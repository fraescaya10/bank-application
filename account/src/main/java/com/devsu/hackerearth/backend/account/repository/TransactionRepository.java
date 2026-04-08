package com.devsu.hackerearth.backend.account.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devsu.hackerearth.backend.account.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t JOIN FETCH t.account")
    List<Transaction> findAllWithAccount();

    @Query("SELECT t FROM Transaction t JOIN FETCH t.account WHERE t.account.clientId = :clientId AND t.date BETWEEN :dateStart AND :dateEnd ORDER BY date DESC")
    List<Transaction> findAllWithAccountByPeriod(@Param(value = "clientId") Long clientId,
            @Param(value = "dateStart") LocalDateTime dateStart, @Param(value = "dateEnd") LocalDateTime dateEnd);

    boolean existsByAccount_Id(Long id);
}
