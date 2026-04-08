package com.devsu.hackerearth.backend.account.service.strategy;

import java.math.BigDecimal;

import com.devsu.hackerearth.backend.account.exception.BalanceNotAvailableException;
import com.devsu.hackerearth.backend.account.model.Account;

public class WithdrawalStrategy implements TransactionStrategy {

    @Override
    public void calculateBalance(BigDecimal amount, Account account, Account targetAccount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new BalanceNotAvailableException();
        }

        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
    }

}
