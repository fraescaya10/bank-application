package com.devsu.hackerearth.backend.account.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankStatementDto {
    
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime date;
	private String client;
	private String accountNumber;
	private String accountType;
	private BigDecimal initialAmount;
    private boolean isActive;
	private String transactionType;
	private BigDecimal amount;
	private BigDecimal balance;
}
