package com.bridgelabz.reportservice.repository;

import com.bridgelabz.reportservice.entity.InventoryReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<InventoryReport, Long> {
}
