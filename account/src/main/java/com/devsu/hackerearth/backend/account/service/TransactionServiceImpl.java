package com.devsu.hackerearth.backend.account.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsu.hackerearth.backend.account.exception.ResourceNotFoundException;
import com.devsu.hackerearth.backend.account.exception.TransactionException;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionType;
import com.devsu.hackerearth.backend.account.model.mapper.TransactionDtoMapper;
import com.devsu.hackerearth.backend.account.repository.TransactionRepository;
import com.devsu.hackerearth.backend.account.service.strategy.DepositStrategy;
import com.devsu.hackerearth.backend.account.service.strategy.TransactionStrategy;
import com.devsu.hackerearth.backend.account.service.strategy.TransferStrategy;
import com.devsu.hackerearth.backend.account.service.strategy.WithdrawalStrategy;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    private final TransactionRepository transactionRepository;
    private final AccountTransactionService accountTransactionService;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
            AccountTransactionService accountTransactionService) {
        this.transactionRepository = transactionRepository;
        this.accountTransactionService = accountTransactionService;
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionDto> getAll() {
        // Get all transactions
        return this.transactionRepository.findAllWithAccount().stream().map(transaction -> {
            logger.info("Transaction {} has date {} and clientId {}", transaction.getId(), transaction.getDate(),
                    transaction.getAccount().getClientId());
            return TransactionDtoMapper.toDto(transaction, transaction.getAccount().getId());
        }).collect(Collectors.toList());
    }

    @Override
    public TransactionDto getById(Long id) {
        // Get transactions by id
        Transaction transaction = this.transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", id));
        return TransactionDtoMapper.toDto(transaction, transaction.getAccount().getId());
    }

    @Transactional
    @Override
    public List<TransactionDto> create(TransactionDto transactionDto) {
        // Create transaction

        try {
            logger.info("Attempting to create transaction with type={}", transactionDto.getType());

            if (null == transactionDto.getAccountId()) {
                throw new TransactionException("AccountId for the transaction is null");
            }

            transactionDto.setAmount(transactionDto.getAmount().abs());

            if (transactionDto.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                throw new TransactionException("Transaction amount shouldn't be zero");
            }

            Account transactionAccount = this.accountTransactionService
                    .getAccountByAccountId(transactionDto.getAccountId());
            if (!transactionAccount.isActive()) {
                throw new TransactionException("Account with id=" + transactionAccount.getId() + " is not active");
            }
            Account targetAccount = null;
            if (transactionDto.getType() == TransactionType.TRANSFERENCIA) {
                if (null == transactionDto.getTargetAccountId()) {
                    throw new TransactionException("Target accountId for the transaction is null");
                }
                targetAccount = this.accountTransactionService
                        .getAccountByAccountId(transactionDto.getTargetAccountId());
                if (!targetAccount.isActive()) {
                    throw new TransactionException(
                            "Target account with id=" + targetAccount.getId() + " is not active");
                }
            }

            // Update balance in accounts
            this.updateBalance(transactionAccount, targetAccount, transactionDto);
            // Process transaction/s
            List<Transaction> transactionsSaved = this.processTransaction(transactionAccount, targetAccount,
                    transactionDto);

            logger.info("Transaction with type={} created successfully!", transactionDto.getType());
            return transactionsSaved.stream().map(transaction -> {
                return TransactionDtoMapper.toDto(transaction, transaction.getAccount().getId());
            }).collect(Collectors.toList());

        } catch (TransactionException ex) {
            logger.error("Error on create transaction. Error: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Error on create transaction: ", ex);
            throw new TransactionException(ex.getMessage());
        }
    }

    @Override
    public List<BankStatementDto> getAllByAccountClientIdAndDateBetween(Long clientId, LocalDate dateTransactionStart,
            LocalDate dateTransactionEnd) {
        // Report
        LocalDateTime startDate = dateTransactionStart.atStartOfDay();
        LocalDateTime endDate = dateTransactionEnd.atTime(23, 59, 59);

        return this.transactionRepository.findAllWithAccountByPeriod(clientId, startDate, endDate).stream()
                .map(transaction -> {
                    return TransactionDtoMapper.toBankStatementDto(transaction);
                }).collect(Collectors.toList());
    }

    @Override
    public TransactionDto getLastByAccountId(Long accountId) {
        // If you need it
        return null;
    }

    private TransactionStrategy getTransactionStrategy(TransactionType transactionType) {
        switch (transactionType) {
            case DEPOSITO:
                return new DepositStrategy();
            case RETIRO:
                return new WithdrawalStrategy();
            case TRANSFERENCIA:
                return new TransferStrategy();
            default:
                return null;
        }
    }

    private void updateBalance(Account transactionAccount, Account targetAccount, TransactionDto transactionDto) {
        TransactionStrategy transactionStrategy = this.getTransactionStrategy(transactionDto.getType());
        transactionStrategy.calculateBalance(transactionDto.getAmount(), transactionAccount, targetAccount);
    }

    private List<Transaction> processTransaction(Account transactionAccount, Account targetAccount,
            TransactionDto transactionDto) {
        try {

            logger.info("Processing transaction with type={}", transactionDto.getType());
            UUID transactionReference = UUID.randomUUID(); // para ubicar las transferencias

            List<Transaction> transactionsToProccess = new ArrayList<>();
            Account transactionAccountSaved = this.accountTransactionService.updateAccount(transactionAccount);

            Transaction newTransaction = TransactionDtoMapper.toEntity(transactionDto);
            newTransaction.setCode(UUID.randomUUID());
            newTransaction.setAccount(transactionAccountSaved);
            newTransaction.setReference(transactionReference);
            newTransaction.setBalance(transactionAccountSaved.getBalance());

            if (null != targetAccount && transactionDto.getType() == TransactionType.TRANSFERENCIA) {
                newTransaction.setType(TransactionType.RETIRO);
                transactionsToProccess.add(newTransaction);

                Account targetAccountSaved = this.accountTransactionService.updateAccount(targetAccount);

                Transaction transferTransaction = TransactionDtoMapper.toEntity(transactionDto);
                transferTransaction.setAccount(targetAccountSaved);
                transferTransaction.setBalance(targetAccountSaved.getBalance());
                transferTransaction.setCode(UUID.randomUUID());
                transferTransaction.setType(TransactionType.DEPOSITO);
                transferTransaction.setReference(transactionReference);
                transactionsToProccess.add(transferTransaction);
            } else if (transactionDto.getType() != TransactionType.TRANSFERENCIA) {
                transactionsToProccess.add(newTransaction);
            }

            List<Transaction> transactionsSaved = this.transactionRepository.saveAll(transactionsToProccess);
            return transactionsSaved;
        } catch (Exception ex) {
            logger.error("Error on process transaction: ", ex);
            throw new TransactionException("Error on proccess transaction with type=" + transactionDto.getType());
        }

    }

}
