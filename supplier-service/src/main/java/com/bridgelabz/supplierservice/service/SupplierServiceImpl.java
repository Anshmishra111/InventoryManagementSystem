package com.bridgelabz.supplierservice.service;

import com.bridgelabz.supplierservice.entity.Supplier;
import com.bridgelabz.supplierservice.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Supplier addSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    public List<Supplier> getAllActiveSuppliers() {
        return supplierRepository.findAllByActiveTrue();
    }

    @Override
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
    }

    @Override
    public Supplier updateSupplier(Long id, Supplier supplierDetails) {
        Supplier supplier = getSupplierById(id);
        supplier.setName(supplierDetails.getName());
        supplier.setContactPerson(supplierDetails.getContactPerson());
        supplier.setEmail(supplierDetails.getEmail());
        supplier.setPhone(supplierDetails.getPhone());
        supplier.setAddress(supplierDetails.getAddress());
        return supplierRepository.save(supplier);
    }

    @Override
    public void deactivateSupplier(Long id) {
        Supplier supplier = getSupplierById(id);
        supplier.setActive(false);
        supplierRepository.save(supplier);
    }
}
