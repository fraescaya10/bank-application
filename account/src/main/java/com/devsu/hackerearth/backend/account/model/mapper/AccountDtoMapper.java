package com.devsu.hackerearth.backend.account.model.mapper;

import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.utils.Utils;

public class AccountDtoMapper {
    public static AccountDto toDto(Account account, boolean withMask) {
        String accountNumber = withMask ? Utils.maskNumber(account.getNumber(), 4) : account.getNumber();
        return new AccountDto(account.getId(), accountNumber, account.getType(),
                account.getInitialAmount(), account.isActive(), account.getClientId());
    }

    public static Account toEntity(AccountDto accountDto) {
        return Account.builder()
                .number(accountDto.getNumber())
                .type(accountDto.getType())
                .initialAmount(accountDto.getInitialAmount())
                .isActive(accountDto.isActive())
                .clientId(accountDto.getClientId())
                .build();
    }

}
