package com.example.shopweb.controller;

import com.example.shopweb.constant.Constant;
import com.example.shopweb.dto.ProductDto;
import com.example.shopweb.dto.ProductImageDto;
import com.example.shopweb.entity.ProductEntity;
import com.example.shopweb.entity.ProductImageEntity;
import com.example.shopweb.exceptions.DataNotFoundException;
import com.example.shopweb.response.ProductListResponse;
import com.example.shopweb.response.ProductResponse;
import com.example.shopweb.service.ICategoryService;
import com.example.shopweb.service.IProductService;
import com.example.shopweb.util.LocalizationUtil;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/product")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;
    private final ICategoryService categoryService;
    private final LocalizationUtil localizationUtil;

    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit) {

        PageRequest pageRequest = PageRequest.of(page,limit,
                Sort.by("createDate").descending());

        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                    .products(products)
                    .totalPages(totalPages)
                    .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") UUID product_id ) {
        try {
            ProductEntity existProduct = productService.getProductById(product_id);
            return ResponseEntity.ok(ProductResponse.fromProduct(existProduct));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID id ) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(String.format("delete product with id = %d successfully",id));
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDto productDto,
            BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            ProductEntity newProduct = productService.createProduct(productDto);
            return ResponseEntity.ok(newProduct);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping(value = "uploads/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") UUID productId,
            @ModelAttribute("files") List<MultipartFile> files){
        try {
            List<ProductImageEntity> imageEntities = new ArrayList<>();
            ProductEntity existProduct = productService.getProductById(productId);

            files = files==null ? new ArrayList<>() : files;

            //Số file upload <= 5
            if(files.size() > ProductImageEntity.MAXIMUM_IMAGES_PER_PRODUCT){
                return ResponseEntity.badRequest().body(ProductResponse.builder()
                                .message(localizationUtil.getLocalizedMessage(Constant.UPLOAD_IMAGES_MAX_5))
                        .build());
            }

            for(MultipartFile file : files){
                if (file != null) {

                    if (file.getSize() == 0) {
                        continue;
                    }

                    //Kiểm tra kích thước file
                    if (file.getSize() > 10 * 1024 * 1024) { //Kích thước > 10MB
                        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(ProductResponse.builder()
                                        .message(localizationUtil.getLocalizedMessage(Constant.UPLOAD_IMAGES_FILE_LARGE))
                                .build());
                    }

                    //kiểm tra định dạng
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(ProductResponse.builder()
                                        .message(localizationUtil.getLocalizedMessage(Constant.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE))
                                .build());
                    }

                    //Lưu file
                    String fileName = storeFile(file);

                    ProductImageEntity productImage = productService.createProductImage(ProductImageDto.builder()
                            .imageUrl(fileName)
                            .build(),
                            existProduct.getId());
                    imageEntities.add(productImage);
                }
            }
            return ResponseEntity.ok().body(imageEntities);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> getImages(@PathVariable String imageName){
        try {
            Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource urlResource = new UrlResource(imagePath.toUri());

            if(urlResource.exists()){
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(urlResource);
            }else {
                return ResponseEntity.notFound().build();
            }

        }catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        //Thêm UUID vào trước tên file để đảm bảo file là duy nhất
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

        //Đường dẫn lưu file
        Path uploadDir = Paths.get("uploads");

        //Kiểm tra và tạo thư mục nếu không tồn tại
        if (!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }

        //Đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);

        //Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    @PostMapping("/generateFakeProducts")
    public ResponseEntity<String> generateFakeProducts(){
        Faker faker = new Faker();

        //Lấy danh sách id từ category
        List<UUID> categoryIds = categoryService.getAllCategoryIds();

        for (int i=0;i<1000;i++){
            String productName = faker.commerce().productName();
            if (productService.existsByName(productName)){
                continue;
            }

            //Chọn ngẫu nhiên một category ID có trong danh sách
            Random random = new Random();
            UUID randomCategoryId = categoryIds.get(random.nextInt(categoryIds.size()));

            ProductDto productDto = ProductDto.builder()
                    .title(productName)
                    .price(faker.number().numberBetween(10,90_000_000))
                    .thumbnail("")
                    .description(faker.lorem().sentence())
                    .categoryId(randomCategoryId)
                    .build();
            try{
                productService.createProduct(productDto);
            }catch (Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake product generated successfully");
    }

    @PutMapping("")
    public ResponseEntity<?> updateProduct(
            @PathVariable UUID id,@RequestBody ProductDto productDto){
        try {
            ProductEntity updateProduct = productService.updateProduct(productDto,id);
            return ResponseEntity.ok(ProductResponse.fromProduct(updateProduct));
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
