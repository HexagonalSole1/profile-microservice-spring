package com.vallhalatech.profile_service.service;


import com.vallhalatech.profile_service.persistence.entities.Product;
import com.vallhalatech.profile_service.web.dtos.product.request.ProductRequest;
import com.vallhalatech.profile_service.web.dtos.response.BaseResponse;
import org.springframework.stereotype.Service;

@Service
public interface IProductService {
    BaseResponse addProduct(ProductRequest productRequest);
    BaseResponse getProductById(Long id);
    BaseResponse getAllProducts(int page, int size);
    BaseResponse searchProducts(String name, Long categoryId, int page, int size);
    BaseResponse getProductsByCategory(Long categoryId, int page, int size);
    BaseResponse updateProduct(Long id, ProductRequest productRequest);
    BaseResponse updateStock(Long id, Integer stock);
    BaseResponse deleteProduct(Long id);
}
