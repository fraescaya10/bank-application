package com.devsu.hackerearth.backend.account.model.dto;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {

	private Long id;
	private String number;
	@NotNull(message = "You should provide an account type", groups = ValidationGroups.OnCreate.class)
	private AccountType type;
	@DecimalMin(value = "0.01", message = "The initial amount should be greater than zero")
	private BigDecimal initialAmount;
	private boolean isActive;
	@NotNull(message = "Client id is mandatory", groups = ValidationGroups.OnCreate.class)
	private Long clientId;
	private BigDecimal balance;
}
