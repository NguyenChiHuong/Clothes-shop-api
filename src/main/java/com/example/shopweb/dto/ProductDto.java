package com.example.shopweb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto{

    @NotBlank(message = "title is mandatory")
    @Size(min = 3 , max = 200, message = "title must be between 3 and 200 characters")
    private String title;

    @Min(value = 0, message = "Price must be more than or equal to 0")
    @Max(value = 100000000, message = "Price must be less than equal to 100,000,000")
    private float price;

    @Min(value = 0, message = "Discount must be more than or equal to 0")
    @Max(value = 100, message = "Discount must be less than equal to 100")
    private int discount;

    private String thumbnail;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Category ID is not null")
    @JsonProperty("category_id")
    private UUID categoryId;

//    @NotNull(message = "color ID is not null")
    @JsonProperty("color_id")
    private UUID colorId;

    private List<MultipartFile> files;
}