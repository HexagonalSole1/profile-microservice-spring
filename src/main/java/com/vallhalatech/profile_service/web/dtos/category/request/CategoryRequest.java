package com.vallhalatech.profile_service.web.dtos.category.request;// CategoryRequest.java

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class CategoryRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String name;

    @Size(max = 200, message = "La descripción no puede exceder los 200 caracteres")
    private String description;
}



