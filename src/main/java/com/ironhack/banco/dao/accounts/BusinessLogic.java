package com.ironhack.banco.dao.accounts;

import com.ironhack.banco.enums.Status;
import com.ironhack.banco.repository.AccountRepository;
import com.ironhack.banco.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
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

    //Method to establish if the transaction amount exceeds the max amount in the last 24 hours.
    //I set a min to 100 pounds to exclude random small purchases triggering fraud alert.
    //This, however, needs to be provided by the business as often fraudulent transactions are pretty small at start.
    //Also, the logic could potentially look at average/ maximum during a longer period of time.
    public Boolean notExceedMaxAmount(Account account, Transaction transaction)  {
        Date start = addHoursToTransactionDate(transaction.getTransactionTime(), -24);
        var transactionsRecent = transactionRepository.findBySenderIdAndTransactionTimeBetween(
                account.getId(), start, transaction.getTransactionTime());
        BigDecimal maxAmount = findMaxAmount(transactionsRecent);
        if(transactionsRecent.size()>0 && transaction.getTransactionAmount().getAmount().doubleValue()>100 &&
                transaction.getTransactionAmount().getAmount().doubleValue()<=(maxAmount.doubleValue()*1.5)
                ||transaction.getTransactionAmount().getAmount().doubleValue()<=100) {
            return true;
        } else if(transactionsRecent.size()==0 && transaction.getTransactionAmount().getAmount().doubleValue()<=100){
            return true;
        } else {
            return false;
        }
    }

    //Method to establish whether there were more than 1 transaction in a second.
    public Boolean notExceedMaxCount(Account account, Transaction transaction){
        Date start = addSecondsToTransactionDate(transaction.getTransactionTime(), -1);
        var transactionsRecent = transactionRepository.findBySenderIdAndTransactionTimeBetween(
                account.getId(), start, transaction.getTransactionTime());
        if(transactionsRecent.size()<=1){
            return true;
        }
        return false;
    }

    public BigDecimal findMaxAmount(List<Transaction> transactions){
        BigDecimal maxValue = BigDecimal.ZERO;
        for(int i= 0; i< transactions.size(); i++){
            if(transactions.get(i).getTransactionAmount().getAmount().doubleValue()>maxValue.doubleValue()){
                maxValue = transactions.get(i).getTransactionAmount().getAmount();
            }
        }
        return maxValue;
    }

    //util function to add/ deduct time in hours
    public Date addHoursToTransactionDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hours);
        return calendar.getTime();
    }

    //util function to add/ deduct time in seconds
    public Date addSecondsToTransactionDate(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }


}
