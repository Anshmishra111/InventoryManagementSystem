package com.bridgelabz.salesservice.repository;

import com.bridgelabz.salesservice.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
    List<SalesOrder> findAllByCustomerId(Long customerId);
}
