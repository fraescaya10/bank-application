package com.devsu.hackerearth.backend.account.model.mapper;

import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;

public class TransactionDtoMapper {
    public static TransactionDto toDto(Transaction transaction, Long accountId) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .code(transaction.getCode().toString())
                .reference(transaction.getReference().toString())
                .date(transaction.getDate())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .balance(transaction.getBalance())
                .accountId(accountId)
                .build();
    }

    public static Transaction toEntity(TransactionDto transactionDto) {
        return Transaction.builder()
                .date(transactionDto.getDate())
                .type(transactionDto.getType())
                .amount(transactionDto.getAmount())
                .build();
    }

    public static BankStatementDto toBankStatementDto(Transaction transaction) {
        Account account = transaction.getAccount();
        return BankStatementDto.builder()
                .client(account.getClientName())
                .date(transaction.getDate())
                .accountNumber(account.getNumber())
                .accountType(String.valueOf(account.getType()))
                .initialAmount(account.getInitialAmount())
                .isActive(account.isActive())
                .transactionType(String.valueOf(transaction.getType()))
                .amount(transaction.getAmount())
                .balance(transaction.getBalance())
                .build();
    }
}
