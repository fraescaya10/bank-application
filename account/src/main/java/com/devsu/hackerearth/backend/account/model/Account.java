package com.devsu.hackerearth.backend.account.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrePersist;

import com.devsu.hackerearth.backend.account.model.dto.AccountType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends Base {

    @Column(nullable = false, unique = true)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    @Column(nullable = false)
    private BigDecimal initialAmount;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private boolean isActive;

    @Column(name = "client_id")
    private Long clientId;

    @Column
    private boolean isDeleted;

    @PrePersist
    protected void onCreateAccount() {
        this.isActive = false;
    }
}
