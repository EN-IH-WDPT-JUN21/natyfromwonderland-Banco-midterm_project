package com.ironhack.banco.controller.impl;

import com.ironhack.banco.controller.interfaces.IAccountController;
import com.ironhack.banco.dao.accounts.*;
import com.ironhack.banco.dao.utils.Money;
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

    @GetMapping("/index")
    @ResponseStatus(HttpStatus.OK)
    public String index(){ //the actual function from the interface
        return "Welcome to Banco!";
    }

    //Route to create a new checking account
    @PostMapping("/accounts/create/checking")
    @ResponseStatus(HttpStatus.CREATED)
    public Checking createNewChecking(@RequestBody @Valid Checking checking) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        Date date = dateFormat.parse(today.toString());
        if(checking.checkPrimaryOwnerAge(date).intValue()>=24) {
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        Date date = dateFormat.parse(today.toString());
        if(studentChecking.checkPrimaryOwnerAge(date)<24) {
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        Date date = dateFormat.parse(today.toString());
        if(creditCard.checkPrimaryOwnerAge(date)>=18) {
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

    @PatchMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(@PathVariable("id") Long id, @RequestBody @Valid Money balance){
        accountService.updateBalance(id, balance);
    }

    @GetMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account getById(@PathVariable(name = "id") Long id) throws ParseException {
        Optional<Account> account = accountRepository.findById(id);

        if(account.isPresent()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();
            Date date = dateFormat.parse(today.toString());

            if (account.get() instanceof Checking) {
                account.get().setBalance(((Checking) account.get()).applyFees(date));
                accountRepository.save(account.get());
                return account.get();
            } else if (account.get() instanceof StudentChecking) {
                return account.get();
            } else if (account.get() instanceof Savings) {
                account.get().setBalance(((Savings) account.get()).applyInterest(date));
                accountRepository.save(account.get());
                return account.get();
            } else if (account.get() instanceof CreditCard) {
                account.get().setBalance(((CreditCard) account.get()).applyInterest(date));
                accountRepository.save(account.get());
                return account.get();
            }
        }
        return null;
    }

}
