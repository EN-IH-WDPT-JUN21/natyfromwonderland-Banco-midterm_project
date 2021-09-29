package com.ironhack.banco.controller.impl;

import com.ironhack.banco.controller.interfaces.IAccountController;
import com.ironhack.banco.dao.accounts.*;
import com.ironhack.banco.dao.utils.Money;
import com.ironhack.banco.dao.utils.ThirdParty;
import com.ironhack.banco.dto.TransactionDTO;
import com.ironhack.banco.repository.*;
import com.ironhack.banco.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class AccountController implements IAccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CheckingRepository checkingRepository;

    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    @Autowired
    private SavingsRepository savingsRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private BusinessLogic businessLogic;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/index")
    @ResponseStatus(HttpStatus.OK)
    public String index(){ //the actual function from the interface
        return "Welcome to Banco!";
    }

    //Route to create a new checking account
    @PostMapping("/accounts/create/checking")
    @ResponseStatus(HttpStatus.CREATED)
    public Checking createNewChecking(@RequestBody @Valid Checking checking) throws ParseException {
        Date today = new Date();
        if(checking.checkPrimaryOwnerAge(today).intValue()>=24) {
            return checkingRepository.save(checking);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Checking account is only created for " +
                    "custmers over 24 years old. Please, create a student checking account");
        }
    }

    //Route to create a new  student checking account
    @PostMapping("/accounts/create/studentchecking")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentChecking createStudentChecking(@RequestBody @Valid StudentChecking studentChecking) throws ParseException {
        Date today = new Date();
        if(studentChecking.checkPrimaryOwnerAge(today)<24) {
            return studentCheckingRepository.save(studentChecking);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student checking account is only created for " +
                    "custmers less than 24 years old. Please, create a normal checking account");
        }
    }

    //Route to create a new credit card account (assuming it's only available for adults)
    @PostMapping("/accounts/create/creditcard")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCard createNewCreditCard(@RequestBody @Valid CreditCard creditCard) throws ParseException {
        Date today = new Date();
        if(creditCard.checkPrimaryOwnerAge(today)>=18) {
                return creditCardRepository.save(creditCard);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credit Card account is only created for " +
                        "custmers over 18 years old");
            }
    }

    //Route to create a new savings account
    @PostMapping("/accounts/create/savings")
    @ResponseStatus(HttpStatus.CREATED)
    public Savings createNewSavings(@RequestBody @Valid Savings savings){
        return savingsRepository.save(savings);
    }

    //route to update account balance
    @PatchMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(@PathVariable("id") Long id, @RequestBody @Valid Money balance){
        accountService.updateBalance(id, balance);
    }

    //route to get account by account id
    @GetMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account getById(@PathVariable(name = "id") Long id) throws ParseException {
        Optional<Account> account = accountRepository.findById(id);

        if(account.isPresent()) {
            Date today = new Date();

            if (account.get() instanceof Checking) {
                account.get().setBalance(((Checking) account.get()).applyFees(today));
                accountRepository.save(account.get());
                return account.get();
            } else if (account.get() instanceof StudentChecking) {
                return account.get();
            } else if (account.get() instanceof Savings) {
                account.get().setBalance(((Savings) account.get()).applyInterest(today));
                accountRepository.save(account.get());
                return account.get();
            } else if (account.get() instanceof CreditCard) {
                account.get().setBalance(((CreditCard) account.get()).applyInterest(today));
                accountRepository.save(account.get());
                return account.get();
            }
        }
        return null;
    }

    //route to get account balance by user Id
    @GetMapping("/accounts/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getByUserId(@PathVariable(name = "id") Long id) throws ParseException {
        Optional<Account> account = accountRepository.findByPrimaryOwnerId(id);

        if(account.isPresent()) {
            Date today = new Date();

            if (account.get() instanceof Checking) {
                account.get().setBalance(((Checking) account.get()).applyFees(today));
                accountRepository.save(account.get());
                return account.get().getBalance();
            } else if (account.get() instanceof StudentChecking) {
                return account.get().getBalance();
            } else if (account.get() instanceof Savings) {
                account.get().setBalance(((Savings) account.get()).applyInterest(today));
                accountRepository.save(account.get());
                return account.get().getBalance();
            } else if (account.get() instanceof CreditCard) {
                account.get().setBalance(((CreditCard) account.get()).applyInterest(today));
                accountRepository.save(account.get());
                return account.get().getBalance();
            }
        }
        return null;
    }

    //route to return the list of accounts
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    //route to receive money
    @PostMapping("/accounts/receivemoney")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction receiveMoney(@RequestBody TransactionDTO transactionDTO){
        Optional<Account> optionalAccount = accountRepository.findById(transactionDTO.getAccountId());
        Date date = new Date();
        if(optionalAccount.isPresent()){
            Transaction transaction = new Transaction(transactionDTO.getTransactionAmount(), date,
                    optionalAccount.get().getId());
            transactionRepository.save(transaction);
            if(businessLogic.notExceedMaxAmount(optionalAccount.get(), transaction)
                    && businessLogic.notExceedMaxCount(optionalAccount.get(), transaction)){
                optionalAccount.get().receiveMoney(transaction.getTransactionAmount());
                accountRepository.save(optionalAccount.get());
            } else {
                businessLogic.freezeAcc(optionalAccount.get());
            }
            return transaction;
        }
        return null;
    }

    //route to send money
    @PostMapping("/accounts/send")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction sendMoney(@RequestBody TransactionDTO transactionDTO) throws Exception {
        var optionalAccount = accountRepository.findById(transactionDTO.getAccountId());
        Date date = new Date();
        if(optionalAccount.isPresent()){
            Transaction transaction = new Transaction(transactionDTO.getTransactionAmount(), date,
                    optionalAccount.get().getId());
            transactionRepository.save(transaction);
            if(businessLogic.notExceedMaxAmount(optionalAccount.get(), transaction)
                    && businessLogic.notExceedMaxCount(optionalAccount.get(), transaction)){
                if(optionalAccount.get() instanceof CreditCard) {
                    ((CreditCard) optionalAccount.get()).sendMoneyCC(transaction.getTransactionAmount());
                } else if(optionalAccount.get() instanceof Checking){
                    optionalAccount.get().sendMoney(transaction.getTransactionAmount());
                } else if(optionalAccount.get() instanceof Savings) {
                    optionalAccount.get().sendMoney(transaction.getTransactionAmount());
                } else if(optionalAccount.get() instanceof StudentChecking){
                    optionalAccount.get().sendMoney(transaction.getTransactionAmount());
                }
                businessLogic.freezeAcc(optionalAccount.get());
            }
            accountRepository.save(optionalAccount.get());
            return  transaction;
        }
        return  null;
    }


}
