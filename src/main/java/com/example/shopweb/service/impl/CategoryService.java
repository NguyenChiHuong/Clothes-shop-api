package com.example.shopweb.service.impl;

import com.example.shopweb.dto.CategoryDto;
import com.example.shopweb.entity.CategoryEntity;
import com.example.shopweb.repository.CategoryRepository;
import com.example.shopweb.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryEntity getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Category not found"));
    }

    @Override
    public CategoryEntity createCategory(CategoryDto categoryDto) {
        CategoryEntity newCategory = CategoryEntity.builder()
                .name(categoryDto.getName())
                .build();//convert
        return categoryRepository.save(newCategory);
    }

    @Override
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryEntity updateCategory(CategoryDto categoryDto, UUID id) {
        CategoryEntity existCategory = getCategoryById(id);
        existCategory.setName(categoryDto.getName());
        categoryRepository.save(existCategory);
        return existCategory;
    }

    @Override
    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<UUID> getAllCategoryIds() {
        List<UUID> categoryIds = categoryRepository.findAll()
                .stream()
                .map(CategoryEntity::getId)
                .collect(Collectors.toList());
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new RuntimeException("No categories found in the database");
        }
        return categoryIds;
    }
}
