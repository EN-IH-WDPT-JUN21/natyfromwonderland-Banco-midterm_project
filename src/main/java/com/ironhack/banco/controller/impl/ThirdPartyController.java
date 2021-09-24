package com.ironhack.banco.controller.impl;

import com.ironhack.banco.controller.interfaces.IThirdPartyController;
import com.ironhack.banco.dao.accounts.*;
import com.ironhack.banco.dao.utils.ThirdParty;
import com.ironhack.banco.dto.TransactionDTO;
import com.ironhack.banco.repository.AccountRepository;
import com.ironhack.banco.repository.ThirdPartyRepository;
import com.ironhack.banco.repository.TransactionRepository;
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
    private TransactionRepository transactionRepository;

    @Autowired
    private BusinessLogic businessLogic;

    //Route to create a third party
    @PostMapping("/thirdparty")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty addParty(@RequestBody @Valid ThirdParty thirdParty){
        return thirdPartyRepository.save(thirdParty);
    }

    @PostMapping("/thirdparty/receivemoney")
    public void receiveMoney(@RequestParam (name = "hashedkey") String hashedKey, @RequestBody TransactionDTO transactionDTO) throws Exception {
        Optional<Account> optionalAccount = accountRepository.findById(transactionDTO.getAccountId());
        if(thirdPartyRepository.findByHashedKey(hashedKey).isPresent() && optionalAccount.isPresent()){
            Transaction transaction = new Transaction();
            transaction.setTransactionAmount(transactionDTO.getTransactionAmount());
            transaction.setTransactionTime(new Date());
            transaction.setAccount(optionalAccount.get());
            transactionRepository.save(transaction);
            if(businessLogic.notExceedMaxAmount(optionalAccount.get(), transaction)
            && businessLogic.notExceedMaxCount(optionalAccount.get(), transaction)){
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
            businessLogic.freezeAcc(optionalAccount.get());
            transactionRepository.delete(transaction);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No parameters found");
    }

    @PostMapping("/thirdparty/sendmoney")
    public void sendMoney(@RequestBody TransactionDTO transactionDTO) {
        Optional<Account> optionalAccount = accountRepository.findById(transactionDTO.getAccountId());
        if(thirdPartyRepository.findByHashedKey(transactionDTO.getHashedKey()).isPresent() && optionalAccount.isPresent()){
            Transaction transaction = new Transaction();
            transaction.setTransactionAmount(transactionDTO.getTransactionAmount());
            transaction.setAccount(optionalAccount.get());
            transaction.setTransactionTime(new Date());
            transactionRepository.save(transaction);
            if(businessLogic.notExceedMaxAmount(optionalAccount.get(), transaction)
                    && businessLogic.notExceedMaxCount(optionalAccount.get(), transaction)){
                optionalAccount.get().addTransaction(transaction);
                optionalAccount.get().receiveMoney(transaction.getTransactionAmount());
                accountRepository.save(optionalAccount.get());
            }
            businessLogic.freezeAcc(optionalAccount.get());
            transactionRepository.delete(transaction);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No parameters found");
    }
}
