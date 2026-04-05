package com.devsu.hackerearth.backend.account.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.account.exception.AlreadyExistsException;
import com.devsu.hackerearth.backend.account.exception.NotFoundException;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.model.mapper.AccountDtoMapper;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<AccountDto> getAll() {
        // Get all accounts
        return this.accountRepository.findAll().stream().map(AccountDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto getById(Long id) {
        // Get accounts by id
        Account account = this.accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account", id));
        return AccountDtoMapper.toDto(account);
    }

    @Override
    public AccountDto create(AccountDto accountDto) {

        if (this.accountRepository.existsByNumber(accountDto.getNumber())) {
            throw new AlreadyExistsException("Account number already exists");
        }
        // Create account
        Account accountSaved = this.accountRepository.save(AccountDtoMapper.toEntity(accountDto));
        return AccountDtoMapper.toDto(accountSaved);
    }

    @Override
    public AccountDto update(AccountDto accountDto) {
        // Update account
        return null;
    }

    @Override
    public AccountDto partialUpdate(Long id, PartialAccountDto partialAccountDto) {
        // Partial update account
        return null;
    }

    @Override
    public void deleteById(Long id) {
        // Delete account
    }

}
