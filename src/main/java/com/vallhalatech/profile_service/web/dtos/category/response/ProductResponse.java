package com.vallhalatech.profile_service.web.dtos.category.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private CategoryResponse category;
    private Integer stock;
    private String sku;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
}
