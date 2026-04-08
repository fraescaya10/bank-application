package com.devsu.hackerearth.backend.account.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.query.Param;

import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;

public interface TransactionService {

    public List<TransactionDto> getAll();
	public TransactionDto getById(Long id);
	public List<TransactionDto> create(TransactionDto transactionDto);
    public List<BankStatementDto> getAllByAccountClientIdAndDateBetween(Long clientId, @Param("dateTransactionStart") LocalDate dateTransactionStart, @Param("dateTransactionEnd") LocalDate dateTransactionEnd);
    public TransactionDto getLastByAccountId(Long accountId);
}
