package com.example.stapubox.repositoryDao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.stapubox.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    UserEntity findByEmail(String email);
}
