package com.salescope.service;

import com.salescope.entity.Product;
import com.salescope.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public List<Product> getProductsBySeller(Long sellerId) {
        return productRepository.findBySellerIdAndActiveTrue(sellerId);
    }

    // ✅ YE METHOD MISSING THA
    public Product getProductById(Long id){
        return productRepository.findById(id).orElse(null);
    }
}