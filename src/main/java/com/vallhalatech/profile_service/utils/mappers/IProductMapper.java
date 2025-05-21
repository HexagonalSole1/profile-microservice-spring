package com.vallhalatech.profile_service.utils.mappers;

import com.vallhalatech.profile_service.persistence.entities.Product;
import com.vallhalatech.profile_service.web.dtos.category.response.ProductResponse;
import com.vallhalatech.profile_service.web.dtos.product.request.ProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ICategoryMapper.class})
public interface IProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product product);
}