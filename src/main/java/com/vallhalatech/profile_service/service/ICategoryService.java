package com.vallhalatech.profile_service.service;


import com.vallhalatech.profile_service.web.dtos.category.request.CategoryRequest;
import com.vallhalatech.profile_service.web.dtos.response.BaseResponse;
import org.springframework.stereotype.Service;

@Service
public interface ICategoryService {
    BaseResponse addCategory(CategoryRequest categoryRequest);
    BaseResponse getCategoryById(Long id);
    BaseResponse getAllCategories();
    BaseResponse updateCategory(Long id, CategoryRequest categoryRequest);
    BaseResponse deleteCategory(Long id);
}