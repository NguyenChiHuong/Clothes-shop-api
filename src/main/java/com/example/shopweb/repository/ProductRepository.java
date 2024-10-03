package com.example.shopweb.repository;

import com.example.shopweb.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    boolean existsByName(String name);

    Page<ProductEntity> findAll(Pageable pageable);
}