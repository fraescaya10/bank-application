package com.devsu.hackerearth.backend.account.model.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDto {

	private Long id;
	private String number;
	private AccountType type;
	private BigDecimal initialAmount;
	private boolean isActive;
	private Long clientId;
}
