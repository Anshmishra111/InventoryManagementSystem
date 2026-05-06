package com.bridgelabz.supplierservice.service;

import com.bridgelabz.supplierservice.entity.Supplier;
import java.util.List;

public interface SupplierService {
    Supplier addSupplier(Supplier supplier);
    List<Supplier> getAllActiveSuppliers();
    Supplier getSupplierById(Long id);
    Supplier updateSupplier(Long id, Supplier supplier);
    void deactivateSupplier(Long id);
}
