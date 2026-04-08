package com.devsu.hackerearth.backend.account.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@GetMapping
	public ResponseEntity<List<TransactionDto>> getAll() {
		// api/transactions
		// Get all transactions
		List<TransactionDto> transactions = this.transactionService.getAll();
		return new ResponseEntity<>(transactions, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TransactionDto> get(@PathVariable Long id) {
		// api/transactions/{id}
		// Get transactions by id
		TransactionDto transactionDto = this.transactionService.getById(id);
		return new ResponseEntity<>(transactionDto, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<List<TransactionDto>> create(@RequestBody TransactionDto transactionDto) {
		// api/transactions
		// Create transactions
		List<TransactionDto> transactionsCreated = this.transactionService.create(transactionDto);
		return new ResponseEntity<>(transactionsCreated, HttpStatus.OK);
	}

	@GetMapping("/clients/{clientId}/report")
	public ResponseEntity<List<BankStatementDto>> report(@PathVariable Long clientId,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTransactionStart,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTransactionEnd) {
		// api/transactions/clients/{clientId}/report
		// Get report
		List<BankStatementDto> report = this.transactionService.getAllByAccountClientIdAndDateBetween(clientId,
				dateTransactionStart, dateTransactionEnd);
		return new ResponseEntity<>(report, HttpStatus.OK);
	}
}
