package com.devsu.hackerearth.backend.account.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.devsu.hackerearth.backend.account.model.dto.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends Base {

	@Column(nullable = false)
	private UUID code; //codigo de transaccion

	@Column(nullable = false)
	private UUID reference; 

	@Column(nullable=false)
	private LocalDateTime date;

	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private TransactionType type;

	@Column(nullable=false)
	private BigDecimal amount;

	@Column(nullable=false)
	private BigDecimal balance;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="account_id", nullable = false)
	private Account account;

}
