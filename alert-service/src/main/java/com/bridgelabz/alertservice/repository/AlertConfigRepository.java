package com.bridgelabz.alertservice.repository;

import com.bridgelabz.alertservice.entity.AlertConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlertConfigRepository extends JpaRepository<AlertConfig, Long> {
    Optional<AlertConfig> findByProductId(Long productId);
}
