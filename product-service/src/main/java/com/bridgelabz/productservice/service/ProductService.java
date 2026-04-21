package com.bridgelabz.productservice.service;

import com.bridgelabz.productservice.entity.Product;
import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    Product updateProduct(Long id, Product productDetails);
    Product getProductById(Long id);
    Product getProductBySku(String sku);
    List<Product> getAllActiveProducts();
    List<Product> getLowStockProducts();
    void softDeleteProduct(Long id);
    List<Product> getProductsByCategory(String category);
}
