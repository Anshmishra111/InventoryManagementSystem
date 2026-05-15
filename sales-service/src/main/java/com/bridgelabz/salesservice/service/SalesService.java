package com.bridgelabz.salesservice.service;

import com.bridgelabz.salesservice.entity.Customer;
import com.bridgelabz.salesservice.entity.SalesOrder;
import java.util.List;

public interface SalesService {
    Customer addCustomer(Customer customer);
    List<Customer> getAllCustomers();
    SalesOrder placeOrder(SalesOrder order);
    List<SalesOrder> getOrdersByCustomerId(Long customerId);
    List<SalesOrder> getAllOrders();
    SalesOrder updateStatus(Long id, String status);
}
