package com.bridgelabz.productservice.service;

import com.bridgelabz.productservice.entity.Product;
import com.bridgelabz.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Product createProduct(Product product) {
        if (productRepository.existsBySku(product.getSku())) {
            throw new RuntimeException("Product with SKU " + product.getSku() + " already exists");
        }
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, Product details) {
        Product product = getProductById(id);
        
        product.setName(details.getName());
        product.setDescription(details.getDescription());
        product.setCategory(details.getCategory());
        product.setBrand(details.getBrand());
        product.setUnitOfMeasure(details.getUnitOfMeasure());
        product.setCostPrice(details.getCostPrice());
        product.setSellingPrice(details.getSellingPrice());
        product.setCurrentStockLevel(details.getCurrentStockLevel());
        product.setReorderLevel(details.getReorderLevel());
        product.setMaxStockLevel(details.getMaxStockLevel());
        product.setLeadTimeDays(details.getLeadTimeDays());
        product.setImageUrl(details.getImageUrl());
        product.setBarcode(details.getBarcode());
        
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    public Product getProductBySku(String sku) {
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Product not found with SKU: " + sku));
    }

    @Override
    public List<Product> getAllActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }

    @Override
    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }

    @Override
    @Transactional
    public void softDeleteProduct(Long id) {
        Product product = getProductById(id);
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }
}
