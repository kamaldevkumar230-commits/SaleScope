package com.salescope.repository;

import com.salescope.entity.Order;   // ✅ Correct import
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdAndStatus(Long userId, String status);
    List<Order> findByUser_IdAndStatus(Long userId, String status);

}