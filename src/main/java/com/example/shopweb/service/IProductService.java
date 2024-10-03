package com.example.shopweb.service;

import com.example.shopweb.dto.ProductDto;
import com.example.shopweb.dto.ProductImageDto;
import com.example.shopweb.entity.ProductEntity;
import com.example.shopweb.entity.ProductImageEntity;
import com.example.shopweb.exceptions.DataNotFoundException;
import com.example.shopweb.exceptions.InvalidParentException;
import com.example.shopweb.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface IProductService {
    ProductEntity createProduct(ProductDto productDto) throws DataNotFoundException;

    ProductEntity updateProduct(ProductDto productDto,UUID id) throws DataNotFoundException;

    ProductEntity getProductById(UUID id) throws DataNotFoundException;

    Page<ProductResponse> getAllProducts(PageRequest pageRequest);

    void deleteProduct(UUID id);

    boolean existsByName(String name);

    ProductImageEntity createProductImage(ProductImageDto productImageDto, UUID productId) throws DataNotFoundException, InvalidParentException;
}
