package com.ironhack.banco.service.interfaces;

import com.ironhack.banco.dao.utils.Money;

public interface IAccountService {

    void updateBalance(Long id, Money balance);

}
