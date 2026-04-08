package com.devsu.hackerearth.backend.client.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsu.hackerearth.backend.client.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByDniAndIsDeletedFalse(String dni);
    boolean existsByEmailAndIsDeletedFalse(String email);
    List<Client> findAllByIsDeletedFalse();
    Optional<Client> findByIdAndIsDeletedFalse(Long id);
}
