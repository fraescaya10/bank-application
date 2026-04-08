package com.devsu.hackerearth.backend.account.service.strategy;

import java.math.BigDecimal;

import com.devsu.hackerearth.backend.account.model.Account;

public interface TransactionStrategy {
    void calculateBalance(BigDecimal amount, Account account, Account targetAccount);
}
