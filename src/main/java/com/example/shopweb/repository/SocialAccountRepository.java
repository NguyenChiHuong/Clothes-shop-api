package com.example.shopweb.repository;

import com.example.shopweb.entity.SocialAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccountEntity, UUID> {
}