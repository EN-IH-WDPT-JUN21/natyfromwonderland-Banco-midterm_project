package com.ironhack.banco.controller.impl;

import com.ironhack.banco.controller.interfaces.IThirdPartyController;
import com.ironhack.banco.dao.accounts.*;
import com.ironhack.banco.dao.utils.ThirdParty;
import com.ironhack.banco.dto.TransactionDTO;
import com.ironhack.banco.repository.AccountRepository;
import com.ironhack.banco.repository.ThirdPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@RestController
public class ThirdPartyController implements IThirdPartyController {

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BusinessLogic businessLogic;

    //Route to create a third party
    @PostMapping("/thirdparty")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty addParty(@RequestBody @Valid ThirdParty thirdParty){
        return thirdPartyRepository.save(thirdParty);
    }

    @PostMapping("/thirdparty/sendmoney")
    public void sendMoney(@RequestParam String hashedKey, @RequestBody TransactionDTO transactionDTO) throws Exception {
        Optional<Account> optionalAccount = accountRepository.findById(transactionDTO.getAccountId());
        if(thirdPartyRepository.findByHashedKey(hashedKey).isPresent() && optionalAccount.isPresent()){
            Transaction transaction = new Transaction();
            if(businessLogic.notExceedMaxAmount(optionalAccount.get(), transaction, transactionDTO.getTransactionAmount().getAmount())
            && businessLogic.notExceedMaxCount(optionalAccount.get(), transaction)){
                transaction.setTransactionAmount(transactionDTO.getTransactionAmount());
                transaction.setTransactionTime(new Timestamp(System.currentTimeMillis()));
                transaction.setAccount(optionalAccount.get());
                optionalAccount.get().addTransaction(transaction);
                if(optionalAccount.get() instanceof CreditCard) {
                    ((CreditCard) optionalAccount.get()).applyInterest(new Date());
                    ((CreditCard) optionalAccount.get()).sendMoneyCC(transaction.getTransactionAmount());
                } else if(optionalAccount.get() instanceof Checking){
                    ((Checking) optionalAccount.get()).applyFees(new Date());
                    optionalAccount.get().sendMoney(transaction.getTransactionAmount());
                } else {
                    optionalAccount.get().sendMoney(transaction.getTransactionAmount());
                }
                accountRepository.save(optionalAccount.get());
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No parameters found");
    }

    @PostMapping("/thirdparty/receivemoney")
    public void receiveMoney(@RequestParam String hashedKey, @RequestBody TransactionDTO transactionDTO) {
        Optional<Account> optionalAccount = accountRepository.findById(transactionDTO.getAccountId());
        if(thirdPartyRepository.findByHashedKey(hashedKey).isPresent() && optionalAccount.isPresent()){
            Transaction transaction = new Transaction();
            if(businessLogic.notExceedMaxAmount(optionalAccount.get(), transaction, transactionDTO.getTransactionAmount().getAmount())
                    && businessLogic.notExceedMaxCount(optionalAccount.get(), transaction)){
                transaction.setTransactionAmount(transactionDTO.getTransactionAmount());
                transaction.setTransactionTime(new Timestamp(System.currentTimeMillis()));
                transaction.setAccount(optionalAccount.get());
                optionalAccount.get().addTransaction(transaction);
                optionalAccount.get().receiveMoney(transaction.getTransactionAmount());
                accountRepository.save(optionalAccount.get());
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No parameters found");
    }
}
