package com.example.stapubox.repositoryDao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.stapubox.entities.SportEntity;

public interface SportRepository extends JpaRepository<SportEntity, Long> {
    boolean existsBySportId(Long sportId);
}
