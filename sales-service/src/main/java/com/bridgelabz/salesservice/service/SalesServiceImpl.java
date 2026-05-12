package com.bridgelabz.salesservice.service;

import com.bridgelabz.salesservice.entity.Customer;
import com.bridgelabz.salesservice.entity.SalesOrder;
import com.bridgelabz.salesservice.repository.CustomerRepository;
import com.bridgelabz.salesservice.repository.SalesOrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SalesServiceImpl implements SalesService {

    private final CustomerRepository customerRepository;
    private final SalesOrderRepository salesOrderRepository;

    public SalesServiceImpl(CustomerRepository customerRepository, SalesOrderRepository salesOrderRepository) {
        this.customerRepository = customerRepository;
        this.salesOrderRepository = salesOrderRepository;
    }

    @Override
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public SalesOrder placeOrder(SalesOrder order) {
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("COMPLETED");
        return salesOrderRepository.save(order);
    }

    @Override
    public List<SalesOrder> getOrdersByCustomerId(Long customerId) {
        return salesOrderRepository.findAllByCustomerId(customerId);
    }

    @Override
    public List<SalesOrder> getAllOrders() {
        return salesOrderRepository.findAll();
    }

    @Override
    public SalesOrder updateStatus(Long id, String status) {
        SalesOrder order = salesOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        order.setStatus(status);
        return salesOrderRepository.save(order);
    }
}
