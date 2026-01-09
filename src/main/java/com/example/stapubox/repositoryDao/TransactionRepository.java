package com.example.stapubox.repositoryDao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.stapubox.entities.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

}
