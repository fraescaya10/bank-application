package com.devsu.hackerearth.backend.account.model.mapper;

import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.client.ClientDto;
import com.devsu.hackerearth.backend.account.utils.Utils;

public class AccountDtoMapper {
    public static AccountDto toDto(Account account, boolean withMask) {
        String accountNumber = withMask ? Utils.maskNumber(account.getNumber(), 4) : account.getNumber();
        return AccountDto.builder()
                .id(account.getId())
                .number(accountNumber)
                .type(account.getType())
                .initialAmount(account.getInitialAmount())
                .isActive(account.isActive())
                .clientId(account.getClientId())
                .balance(account.getBalance())
                .build();
    }

    public static Account toEntity(AccountDto accountDto, ClientDto client) {
        return Account.builder()
                .number(accountDto.getNumber())
                .type(accountDto.getType())
                .initialAmount(accountDto.getInitialAmount())
                .balance(accountDto.getBalance())
                .isActive(accountDto.isActive())
                .clientId(client.getId())
                .clientName(client.getName())
                .build();
    }

}
