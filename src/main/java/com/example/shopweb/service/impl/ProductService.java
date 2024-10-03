package com.example.shopweb.service.impl;

import com.example.shopweb.dto.ProductDto;
import com.example.shopweb.dto.ProductImageDto;
import com.example.shopweb.entity.CategoryEntity;
import com.example.shopweb.entity.ProductEntity;
import com.example.shopweb.entity.ProductImageEntity;
import com.example.shopweb.exceptions.DataNotFoundException;
import com.example.shopweb.exceptions.InvalidParentException;
import com.example.shopweb.repository.CategoryRepository;
import com.example.shopweb.repository.ProductImageRepository;
import com.example.shopweb.repository.ProductRepository;
import com.example.shopweb.response.ProductResponse;
import com.example.shopweb.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Override
    public ProductEntity createProduct(ProductDto productDto) throws DataNotFoundException {
        CategoryEntity existCategoryId = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find category with id: "+productDto.getCategoryId()));

        ProductEntity newProduct = ProductEntity.builder()
                .name(productDto.getTitle())
                .thumbnail("")
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .category(existCategoryId)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public ProductEntity updateProduct(ProductDto productDto,UUID id) throws DataNotFoundException {
        ProductEntity existProduct = getProductById(id);
        if(existProduct!=null){
            CategoryEntity existCategoryId = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(()-> new DataNotFoundException("Cannot find category with id: "+productDto.getCategoryId()));
            existProduct.setCategory(existCategoryId);
            existProduct.setName(productDto.getTitle());
            existProduct.setDescription(productDto.getDescription());
            existProduct.setPrice(productDto.getPrice());
            return productRepository.save(existProduct);
        }
        return null;
    }

    @Override
    public ProductEntity getProductById(UUID id) throws DataNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find product with id = "+id));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest).map(ProductResponse::fromProduct);
    }

    @Override
    public void deleteProduct(UUID id) {
        Optional<ProductEntity> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productEntity -> productRepository.delete(productEntity));
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImageEntity createProductImage(ProductImageDto productImageDto,UUID productId) throws DataNotFoundException, InvalidParentException {
        ProductEntity existProduct = productRepository.findById(productId)
                .orElseThrow(()-> new DataNotFoundException("Cannot find product with id "+productImageDto.getProductId()));
        ProductImageEntity newProductImage = ProductImageEntity.builder()
                .product(existProduct)
                .imageUrl(productImageDto.getImageUrl())
                .build();

        //Không thêm quá 5 sản phẩm
        int size = productImageRepository.findByProductId(productId).size();
        if(size >= ProductImageEntity.MAXIMUM_IMAGES_PER_PRODUCT){
            throw new InvalidParentException("Number of images must be <= "+ProductImageEntity.MAXIMUM_IMAGES_PER_PRODUCT);
        }
        return productImageRepository.save(newProductImage);
    }
}
