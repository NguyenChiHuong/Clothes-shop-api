package com.example.shopweb.controller;

import com.example.shopweb.constant.Constant;
import com.example.shopweb.dto.CategoryDto;
import com.example.shopweb.entity.CategoryEntity;
import com.example.shopweb.response.CategoryResponse;
import com.example.shopweb.service.ICategoryService;
import com.example.shopweb.util.LocalizationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/category")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;
    private final LocalizationUtil localizationUtil;

    @PostMapping("")
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            categoryService.createCategory(categoryDto);
            return ResponseEntity.ok(CategoryResponse.builder()
                            .message(localizationUtil.getLocalizedMessage(Constant.INSERT_CATEGORY_SUCCESSFULLY))
                    .build());
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(CategoryResponse.builder()
                            .message(localizationUtil.getLocalizedMessage(Constant.INSERT_CATEGORY_FAILED,ex.getMessage()))
                    .build());
        }
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryEntity>> getCategories(){
        List<CategoryEntity> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@Valid @PathVariable("id") UUID id){
        try{
            categoryService.getCategoryById(id);
            return ResponseEntity.ok("Get Category ID "+id+" Successfully");
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @Valid @PathVariable UUID id,
            @Valid @RequestBody CategoryDto categoryDto){
        try{
            categoryService.updateCategory(categoryDto,id);
            return ResponseEntity.ok(CategoryResponse.builder()
                            .message(localizationUtil.getLocalizedMessage(Constant.UPDATE_CATEGORY_SUCCESSFULLY))
                    .build());
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(CategoryResponse.builder()
                            .message(ex.getMessage())
                    .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@Valid @PathVariable UUID id){
        //Xóa mềm, cập nhật trường active = false
        try{
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(CategoryResponse.builder()
                            .message(localizationUtil.getLocalizedMessage(Constant.DELETE_CATEGORY_SUCCESSFULLY))
                    .build());
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
