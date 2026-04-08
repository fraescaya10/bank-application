package com.devsu.hackerearth.backend.account.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDto {

	private Long id;
	private String code;
	private String reference;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message = "Date is mandatory")
	@PastOrPresent(message = "The date should not be in the future")
    private LocalDateTime date;
	@NotNull(message = "You should provide a transaction type")
	private TransactionType type;
	@NotNull(message = "Amount is mandatory")
	@DecimalMin(value = "0.01", message = "The amount should be greater than zero")
	private BigDecimal amount;
	private BigDecimal balance;
	@NotNull(message = "Account id is mandatory")
	private Long accountId;
	private Long targetAccountId;
}
