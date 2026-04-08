package com.devsu.hackerearth.backend.account.service;

import com.devsu.hackerearth.backend.account.model.Account;

public interface AccountTransactionService {
    public Account getAccountByAccountId(Long accountId);
    public boolean accountHasTransaction(Long accountId);
    public Account updateAccount(Account account);
}
