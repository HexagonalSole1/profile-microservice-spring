package com.vallhalatech.profile_service.web.dtos.category.response;

import lombok.Data;

@Data
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
}
