package com.salescope.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salescope.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> findByActiveTrue();

	List<Product> findBySellerIdAndActiveTrue(Long sellerId);
	 // 🔥 Delete/edit check ke liye
    java.util.Optional<Product> findByIdAndSellerId(Long id, Long sellerId);

   

    
    
    
}