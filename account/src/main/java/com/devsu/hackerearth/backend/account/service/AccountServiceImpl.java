package com.devsu.hackerearth.backend.account.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsu.hackerearth.backend.account.exception.AccountException;
import com.devsu.hackerearth.backend.account.exception.ResourceNotFoundException;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.model.mapper.AccountDtoMapper;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<AccountDto> getAll() {
        // Get all accounts
        return this.accountRepository.findAllByIsDeletedFalse().stream().map((account) -> {
            return AccountDtoMapper.toDto(account, true);
        }).collect(Collectors.toList());
    }

    @Override
    public AccountDto getById(Long id) {
        // Get accounts by id
        Account account = this.accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", id));
        return AccountDtoMapper.toDto(account, false);
    }

    @Transactional
    @Override
    public AccountDto create(AccountDto accountDto) {

        try {
            logger.info("Attempting to create an account");
            accountDto.setNumber(UUID.randomUUID().toString());// Numero de cuenta generado
            accountDto.setBalance(accountDto.getInitialAmount());
            // Create account
            Account newAccount = AccountDtoMapper.toEntity(accountDto);

            Account accountSaved = this.accountRepository.save(newAccount);
            logger.info("Account created successfully!");

            return AccountDtoMapper.toDto(accountSaved, false);
        } catch (Exception ex) {
            logger.error("Error on create Account ", ex);
            throw new AccountException("Error on create Account: " + ex.getMessage());
        }

    }

    @Transactional
    @Override
    public AccountDto update(Long id, AccountDto accountDto) {
        // Update account

        try {
            logger.info("Attempting to update account with id={} ", id);
            if (null == id) {
                throw new AccountException("Account ID is null");
            }

            Account accountToUpdate = this.accountRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Account", id));

            accountToUpdate.setActive(accountDto.isActive());
            accountToUpdate.setInitialAmount(accountDto.getInitialAmount());

            Account accountUpdated = this.accountRepository.save(accountToUpdate);

            return AccountDtoMapper.toDto(accountUpdated, false);
        } catch (AccountException ex) {
            logger.warn("Error on account update: ", ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Error on account update: ", ex);
            throw new AccountException("Error on account update: " + ex.getMessage());
        }

    }

    @Override
    public AccountDto partialUpdate(Long id, PartialAccountDto partialAccountDto) {
        // Partial update account
        if (null == id) {
            throw new AccountException("Account ID is null");
        }

        Account accountToUpdate = this.accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", id));

        accountToUpdate.setActive(partialAccountDto.isActive());

        Account accountUpdated = this.accountRepository.save(accountToUpdate);

        return AccountDtoMapper.toDto(accountUpdated, false);
    }

    @Override
    public void deleteById(Long id) {
        // Delete account
        if (null == id) {
            throw new AccountException("Account ID is null");
        }

        Account accountToDelete = this.accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", id));
        this.accountRepository.delete(accountToDelete);
    }

}
