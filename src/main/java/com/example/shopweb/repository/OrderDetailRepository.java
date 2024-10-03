package com.example.shopweb.repository;

import com.example.shopweb.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, UUID> {
    List<OrderDetailEntity> findByOrderId(UUID orderId);
}