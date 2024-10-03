package com.example.shopweb.service;

import com.example.shopweb.dto.CategoryDto;
import com.example.shopweb.entity.CategoryEntity;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {
    CategoryEntity getCategoryById(UUID id);

    CategoryEntity createCategory(CategoryDto categoryDto);

    List<CategoryEntity> getAllCategories();

    CategoryEntity updateCategory(CategoryDto CategoryDto, UUID id);

    void deleteCategory(UUID id);

    List<UUID> getAllCategoryIds();
}
