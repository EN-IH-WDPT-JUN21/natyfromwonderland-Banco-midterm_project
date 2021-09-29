package com.ironhack.banco.controller.impl;

import com.ironhack.banco.controller.interfaces.IThirdPartyController;
import com.ironhack.banco.dao.accounts.*;
import com.ironhack.banco.dao.utils.Money;
import com.ironhack.banco.dao.utils.ThirdParty;
import com.ironhack.banco.dto.TransactionDTO;
import com.ironhack.banco.repository.AccountRepository;
import com.ironhack.banco.repository.ThirdPartyRepository;
import com.ironhack.banco.repository.TransactionRepository;
import com.ironhack.banco.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
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

    @Autowired
    private IAccountService accountService;

    //Route to create a third party
    @PostMapping("/thirdparty")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty addParty(@RequestBody @Valid ThirdParty thirdParty){
        return thirdPartyRepository.save(thirdParty);
    }

    //Route for third party to receive money
    @PostMapping("/thirdparty/receivemoney")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction receiveMoney(@RequestBody TransactionDTO transactionDTO) throws Exception {
        var optionalAccount = accountRepository.findById(transactionDTO.getAccountId());
        var optionalThirdParty = thirdPartyRepository.findByHashedKey(transactionDTO.getHashedKey());
        Date date = new Date();
        if(optionalThirdParty.isPresent() && optionalAccount.isPresent()){
            Transaction transaction = new Transaction(transactionDTO.getTransactionAmount(), date,
                    optionalThirdParty.get().getId(), optionalAccount.get().getId());
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

    //Route for third party to send money
    @PostMapping("/thirdparty/sendmoney")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction sendMoney(@RequestBody TransactionDTO transactionDTO) {
        var optionalAccount = accountRepository.findById(transactionDTO.getAccountId());
        var optionalThirdParty = thirdPartyRepository.findByHashedKey(transactionDTO.getHashedKey());
        Date date = new Date();
        if(optionalThirdParty.isPresent() && optionalAccount.isPresent()){
            Transaction transaction = new Transaction(transactionDTO.getTransactionAmount(), date,
                    optionalThirdParty.get().getId(), optionalAccount.get().getId());
            transactionRepository.save(transaction);
            if(businessLogic.notExceedMaxAmount(optionalAccount.get(), transaction)
                    && businessLogic.notExceedMaxCount(optionalAccount.get(), transaction)){
                optionalAccount.get().receiveMoney(transaction.getTransactionAmount());
            } else {
                businessLogic.freezeAcc(optionalAccount.get());
            }
            accountRepository.save(optionalAccount.get());
            return transaction;
        }
        return null;
    }

}
