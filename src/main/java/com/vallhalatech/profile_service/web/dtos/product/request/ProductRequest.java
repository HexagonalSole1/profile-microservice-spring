package com.vallhalatech.profile_service.web.dtos.product.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private Long categoryId;
    private Integer stock;
    private String sku;
}
