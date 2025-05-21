package com.vallhalatech.profile_service.service.impl;

import com.vallhalatech.profile_service.persistence.repositories.ICategoryRepository;
import com.vallhalatech.profile_service.persistence.repositories.IProductRepository;
import com.vallhalatech.profile_service.service.ICategoryService;

import com.vallhalatech.profile_service.persistence.entities.Category;
import com.vallhalatech.profile_service.utils.mappers.ICategoryMapper;
import com.vallhalatech.profile_service.web.dtos.category.request.CategoryRequest;
import com.vallhalatech.profile_service.web.dtos.category.response.CategoryResponse;
import com.vallhalatech.profile_service.web.dtos.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepository categoryRepository;
    private final IProductRepository productRepository;
    private final ICategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(ICategoryRepository categoryRepository,
                               IProductRepository productRepository,
                               ICategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional
    public BaseResponse addCategory(CategoryRequest categoryRequest) {
        try {
            // Verificar si ya existe una categoría con el mismo nombre
            if (categoryRepository.existsByName(categoryRequest.getName())) {
                return BaseResponse.builder()
                        .data(null)
                        .message("Ya existe una categoría con ese nombre")
                        .success(false)
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build();
            }

            // Convertir DTO a entidad
            Category category = categoryMapper.toEntity(categoryRequest);

            // Guardar categoría
            Category savedCategory = categoryRepository.save(category);

            // Convertir entidad a DTO respuesta
            CategoryResponse response = categoryMapper.toResponse(savedCategory);

            return BaseResponse.builder()
                    .data(response)
                    .message("Categoría creada exitosamente")
                    .success(true)
                    .httpStatus(HttpStatus.CREATED)
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .data(null)
                    .message("Error al crear la categoría: " + e.getMessage())
                    .success(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @Override
    public BaseResponse getCategoryById(Long id) {
        try {
            Optional<Category> categoryOpt = categoryRepository.findById(id);
            if (categoryOpt.isEmpty()) {
                return BaseResponse.builder()
                        .data(null)
                        .message("Categoría no encontrada")
                        .success(false)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build();
            }

            CategoryResponse response = categoryMapper.toResponse(categoryOpt.get());

            return BaseResponse.builder()
                    .data(response)
                    .message("Categoría encontrada")
                    .success(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .data(null)
                    .message("Error al obtener la categoría: " + e.getMessage())
                    .success(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @Override
    public BaseResponse getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();
            List<CategoryResponse> categoryResponses = categories.stream()
                    .map(categoryMapper::toResponse)
                    .collect(Collectors.toList());

            return BaseResponse.builder()
                    .data(categoryResponses)
                    .message("Categorías obtenidas exitosamente")
                    .success(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .data(null)
                    .message("Error al obtener las categorías: " + e.getMessage())
                    .success(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @Override
    @Transactional
    public BaseResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        try {
            // Verificar que la categoría exista
            Optional<Category> categoryOpt = categoryRepository.findById(id);
            if (categoryOpt.isEmpty()) {
                return BaseResponse.builder()
                        .data(null)
                        .message("Categoría no encontrada")
                        .success(false)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build();
            }

            // Verificar si ya existe otra categoría con el mismo nombre
            Optional<Category> existingCategory = categoryRepository.findByName(categoryRequest.getName());
            if (existingCategory.isPresent() && !existingCategory.get().getId().equals(id)) {
                return BaseResponse.builder()
                        .data(null)
                        .message("Ya existe otra categoría con ese nombre")
                        .success(false)
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build();
            }

            // Actualizar la categoría
            Category category = categoryOpt.get();
            category.setName(categoryRequest.getName());
            category.setDescription(categoryRequest.getDescription());

            Category updatedCategory = categoryRepository.save(category);
            CategoryResponse response = categoryMapper.toResponse(updatedCategory);

            return BaseResponse.builder()
                    .data(response)
                    .message("Categoría actualizada exitosamente")
                    .success(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .data(null)
                    .message("Error al actualizar la categoría: " + e.getMessage())
                    .success(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @Override
    @Transactional
    public BaseResponse deleteCategory(Long id) {
        try {
            // Verificar que la categoría exista
            boolean exists = categoryRepository.existsById(id);
            if (!exists) {
                return BaseResponse.builder()
                        .data(null)
                        .message("Categoría no encontrada")
                        .success(false)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build();
            }

            // Verificar si hay productos asociados a esta categoría
            boolean hasProducts = productRepository.existsByCategoryId(id);
            if (hasProducts) {
                return BaseResponse.builder()
                        .data(null)
                        .message("No se puede eliminar la categoría porque tiene productos asociados")
                        .success(false)
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build();
            }

            // Eliminar categoría
            categoryRepository.deleteById(id);

            return BaseResponse.builder()
                    .data(null)
                    .message("Categoría eliminada exitosamente")
                    .success(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .data(null)
                    .message("Error al eliminar la categoría: " + e.getMessage())
                    .success(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}