package com.bridgelabz.warehouseservice.service;

import com.bridgelabz.warehouseservice.entity.Warehouse;
import java.util.List;

public interface WarehouseService {
    Warehouse createWarehouse(Warehouse warehouse);
    List<Warehouse> getAllActiveWarehouses();
    Warehouse getWarehouseById(Long id);
    void deleteWarehouse(Long id);
}
