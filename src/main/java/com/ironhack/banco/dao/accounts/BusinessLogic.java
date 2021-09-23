package com.ironhack.banco.dao.accounts;

import com.ironhack.banco.enums.Status;
import com.ironhack.banco.repository.AccountRepository;
import com.ironhack.banco.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
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

    public Boolean notExceedMaxAmount(Account account, Transaction transaction, BigDecimal amount)  {
        Timestamp start = transaction.getTransactionTime();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(start.getTime());
        cal.add(Calendar.HOUR, -24);
        start = new Timestamp(cal.getTime().getTime());
        var transactionsRecent = transactionRepository.findByAccountIdAndTransactionTimeBetween(
                account.getId(), start, transaction.getTransactionTime());
        if(amount.doubleValue()>45 && amount.doubleValue()<=(findMaxAmount(transactionsRecent)).doubleValue()*1.5){
            return true;
        } else if(amount.doubleValue()<=45){
            return true;
        }
        return false;
    }

    public Boolean notExceedMaxCount(Account account, Transaction transaction, BigDecimal amount){
        Timestamp start = transaction.getTransactionTime();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(start.getTime());
        cal.add(Calendar.SECOND, -1);
        start = new Timestamp(cal.getTime().getTime());
        var transactionsRecent = transactionRepository.findByAccountIdAndTransactionTimeBetween(
                account.getId(), start, transaction.getTransactionTime());
        if(transactionsRecent.size()<=1){
            return true;
        }
        return false;
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
