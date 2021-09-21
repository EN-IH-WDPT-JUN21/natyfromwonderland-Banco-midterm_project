package com.ironhack.banco.dao;

import com.ironhack.banco.enums.Status;
import com.ironhack.banco.exceptions.ExceedsMaxAmount;
import com.ironhack.banco.repository.AccountRepository;
import com.ironhack.banco.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.List;

@Component
public class BusinessLogic {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public void freezeAcc(Account account){
        account.setStatus(Status.FROZEN);
    }

    public void notExceedMaxAmount(Account account, Transaction transaction, BigDecimal amount)  {
        Timestamp start = transaction.getTransactionTime();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(start.getTime());
        cal.add(Calendar.HOUR, -24);
        start = new Timestamp(cal.getTime().getTime());
        var transactionsRecent = transactionRepository.findByAccountIdAndTransactionTimeBetween(
                account.getId(), start, transaction.getTransactionTime());
        if(amount.doubleValue()>45 && amount.doubleValue()<=(findMaxAmount(transactionsRecent)).doubleValue()*1.5){
            transaction.setTransactionAmount(new Money(amount));
        } else if(amount.doubleValue()<=45){
            transaction.setTransactionAmount(new Money(amount));
        } else {
            freezeAcc(account);
        }

    }

    public void notExceedMaxCount(Account account, Transaction transaction, BigDecimal amount){
        Timestamp start = transaction.getTransactionTime();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(start.getTime());
        cal.add(Calendar.SECOND, -1);
        start = new Timestamp(cal.getTime().getTime());
        var transactionsRecent = transactionRepository.findByAccountIdAndTransactionTimeBetween(
                account.getId(), start, transaction.getTransactionTime());
        if(transactionsRecent.size()<=1){
            transaction.setTransactionAmount(new Money(amount));
        } else {
            freezeAcc(account);
        }

    }

    public BigDecimal findMaxAmount(List<Transaction> transactions){
        BigDecimal maxValue = new BigDecimal("0");
        for(int i= 0; i< transactions.size(); i++){
            if(transactions.get(i).getTransactionAmount().getAmount().doubleValue()>maxValue.doubleValue()){
                maxValue = transactions.get(i).getTransactionAmount().getAmount();
            }
        }
        return maxValue;
    }


}
