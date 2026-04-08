package com.devsu.hackerearth.backend.account.service;

import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.account.exception.ResourceNotFoundException;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;
import com.devsu.hackerearth.backend.account.repository.TransactionRepository;

@Service
public class AccountTransactionServiceImpl implements AccountTransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountTransactionServiceImpl(AccountRepository accountRepository,
            TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Account getAccountByAccountId(Long accountId) {
        return this.accountRepository.findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", accountId));
    }

    @Override
    public boolean accountHasTransaction(Long accountId) {
        return this.transactionRepository.existsByAccount_Id(accountId);
    }

    @Override
    public Account updateAccount(Account account) {
        return this.accountRepository.save(account);
    }

}
