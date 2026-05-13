package com.bridgelabz.alertservice.repository;

import com.bridgelabz.alertservice.entity.Alert;
import com.bridgelabz.alertservice.entity.AlertType;
import com.bridgelabz.alertservice.entity.AlertSeverity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);
    List<Alert> findByRecipientIdAndIsReadFalse(Long recipientId);
    Long countByRecipientIdAndIsReadFalse(Long recipientId);
    List<Alert> findByRecipientIdAndIsAcknowledgedFalse(Long recipientId);
    List<Alert> findByType(AlertType type);
    List<Alert> findBySeverity(AlertSeverity severity);
    List<Alert> findByRelatedProductId(Long productId);
    List<Alert> findByRelatedProductIdAndRelatedWarehouseId(Long productId, Long warehouseId);
    Optional<Alert> findByRelatedProductIdAndRelatedWarehouseIdAndIsReadFalse(Long productId, Long warehouseId);
}
