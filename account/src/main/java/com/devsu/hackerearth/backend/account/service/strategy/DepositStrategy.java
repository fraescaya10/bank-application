package com.devsu.hackerearth.backend.account.service.strategy;

import java.math.BigDecimal;

import com.devsu.hackerearth.backend.account.model.Account;

public class DepositStrategy implements TransactionStrategy {

    @Override
    public void calculateBalance(BigDecimal amount, Account account, Account targetAccount) {
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
    }

}
