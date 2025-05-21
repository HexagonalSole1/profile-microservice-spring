package com.vallhalatech.profile_service.utils.mappers;

import com.vallhalatech.profile_service.persistence.entities.Category;
import com.vallhalatech.profile_service.web.dtos.category.request.CategoryRequest;
import com.vallhalatech.profile_service.web.dtos.category.response.CategoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICategoryMapper {
    Category toEntity(CategoryRequest request);
    CategoryResponse toResponse(Category category);
}