package com.ironhack.banco.service.impl;

import com.ironhack.banco.dao.accounts.Account;
import com.ironhack.banco.dao.utils.Money;
import com.ironhack.banco.repository.AccountRepository;
import com.ironhack.banco.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    @Autowired
    AccountRepository accountRepository;

    public void updateBalance(Long id, Money balance){
        Optional<Account> storedAccount = accountRepository.findById(id);
        if(storedAccount.isPresent()){
            storedAccount.get().setBalance(balance);
            accountRepository.save(storedAccount.get());
        }
    }
}
