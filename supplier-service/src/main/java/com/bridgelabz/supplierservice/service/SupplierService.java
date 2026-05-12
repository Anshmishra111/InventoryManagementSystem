package com.bridgelabz.supplierservice.service;

import com.bridgelabz.supplierservice.entity.Supplier;
import java.util.List;

public interface SupplierService {
    Supplier addSupplier(Supplier supplier);
    List<Supplier> getAllSuppliers();
    Supplier getSupplierById(Long id);
    Supplier updateSupplier(Long id, Supplier supplier);
    void activateSupplier(Long id);
    void deactivateSupplier(Long id);
}
