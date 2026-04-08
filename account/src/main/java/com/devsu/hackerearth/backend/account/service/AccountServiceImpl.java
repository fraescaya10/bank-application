package com.devsu.hackerearth.backend.account.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsu.hackerearth.backend.account.exception.AccountException;
import com.devsu.hackerearth.backend.account.exception.ClientCallException;
import com.devsu.hackerearth.backend.account.exception.ResourceNotFoundException;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.model.dto.client.ClientDto;
import com.devsu.hackerearth.backend.account.model.mapper.AccountDtoMapper;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;
import com.devsu.hackerearth.backend.account.service.client.ClientServiceClient;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;
    private final AccountTransactionService accountTransactionService;
    private final ClientServiceClient clientServiceClient;

    public AccountServiceImpl(AccountRepository accountRepository,
            AccountTransactionService accountTransactionService,
            ClientServiceClient clientServiceClient) {
        this.accountRepository = accountRepository;
        this.accountTransactionService = accountTransactionService;
        this.clientServiceClient = clientServiceClient;
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
        Account account = this.accountRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", id));
        return AccountDtoMapper.toDto(account, false);
    }

    @Transactional
    @Override
    public AccountDto create(AccountDto accountDto) {

        try {
            logger.info("Attempting to create an account");

            ClientDto client = clientServiceClient.getClient(accountDto.getClientId());
            if (!client.isActive()) {
                throw new AccountException("Client is not active");
            }

            accountDto.setNumber(UUID.randomUUID().toString());// Numero de cuenta generado
            accountDto.setBalance(accountDto.getInitialAmount());
            // Create account
            Account newAccount = AccountDtoMapper.toEntity(accountDto, client);

            Account accountSaved = this.accountRepository.save(newAccount);
            logger.info("Account created successfully!");

            return AccountDtoMapper.toDto(accountSaved, false);
        } catch (AccountException ex) {
            logger.error("Error: {}", ex);
            throw ex;
        } catch (ClientCallException ex) {
            logger.error("Client Error: {}", ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Error: {}", ex);
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

            Account accountToUpdate = this.accountRepository.findByIdAndIsDeletedFalse(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Account", id));

            accountToUpdate.setActive(accountDto.isActive());

            if (null != accountDto.getInitialAmount()) {
                boolean accountHasTransaction = this.accountTransactionService.accountHasTransaction(id);
                if (!accountHasTransaction) {
                    accountToUpdate.setInitialAmount(accountDto.getInitialAmount());
                } else {
                    throw new AccountException("Can't update initial amount. Account has already transactions");
                }
            }

            Account accountUpdated = this.accountRepository.save(accountToUpdate);
            logger.info("Account updated successfully!");
            return AccountDtoMapper.toDto(accountUpdated, false);
        } catch (AccountException ex) {
            logger.error("Error on account update: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Error on update account: ", ex);
            throw new AccountException("Error on account update: " + ex.getMessage());
        }

    }

    @Override
    public AccountDto partialUpdate(Long id, PartialAccountDto partialAccountDto) {
        // Partial update account
        logger.info("Attempting to partial update account with id={}", id);
        if (null == id) {
            throw new AccountException("Account ID is null");
        }

        Account accountToUpdate = this.accountRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", id));

        accountToUpdate.setActive(partialAccountDto.isActive());

        Account accountUpdated = this.accountRepository.save(accountToUpdate);
        logger.info("Account partial updated successfully!");
        return AccountDtoMapper.toDto(accountUpdated, false);
    }

    @Override
    public void deleteById(Long id) {
        // Delete account
        logger.info("Attempting to delete account with id={}", id);
        if (null == id) {
            throw new AccountException("Account ID is null");
        }

        Account accountToDelete = this.accountRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", id));
        accountToDelete.setDeleted(true);

        this.accountRepository.save(accountToDelete);
        logger.info("Account deleted successfully!");
    }

}
