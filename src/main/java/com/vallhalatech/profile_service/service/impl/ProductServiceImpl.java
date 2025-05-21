package com.vallhalatech.profile_service.service.impl;

import com.vallhalatech.profile_service.persistence.entities.Category;
import com.vallhalatech.profile_service.persistence.entities.Product;
import com.vallhalatech.profile_service.persistence.repositories.ICategoryRepository;
import com.vallhalatech.profile_service.persistence.repositories.IProductRepository;
import com.vallhalatech.profile_service.service.IProductService;
import com.vallhalatech.profile_service.utils.mappers.IProductMapper;
import com.vallhalatech.profile_service.web.dtos.category.response.ProductResponse;
import com.vallhalatech.profile_service.web.dtos.product.request.ProductRequest;
import com.vallhalatech.profile_service.web.dtos.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final IProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(IProductRepository productRepository,
                              ICategoryRepository categoryRepository,
                              IProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public BaseResponse addProduct(ProductRequest productRequest) {
        try {
            // Validar que la categoría exista
            Optional<Category> categoryOpt = categoryRepository.findById(productRequest.getCategoryId());
            if (categoryOpt.isEmpty()) {
                return BaseResponse.builder()
                        .data(null)
                        .message("La categoría especificada no existe")
                        .success(false)
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build();
            }

            // Convertir DTO a entidad
            Product product = productMapper.toEntity(productRequest);
            product.setCategory(categoryOpt.get());

            // Guardar producto
            Product savedProduct = productRepository.save(product);

            // Convertir entidad a DTO respuesta
            ProductResponse response = productMapper.toResponse(savedProduct);

            return BaseResponse.builder()
                    .data(response)
                    .message("Producto creado exitosamente")
                    .success(true)
                    .httpStatus(HttpStatus.CREATED)
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .data(null)
                    .message("Error al crear el producto: " + e.getMessage())
                    .success(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @Override
    public BaseResponse getProductById(Long id) {
        try {
            Optional<Product> productOpt = productRepository.findById(id);
            if (productOpt.isEmpty()) {
                return BaseResponse.builder()
                        .data(null)
                        .message("Producto no encontrado")
                        .success(false)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build();
            }

            ProductResponse response = productMapper.toResponse(productOpt.get());

            return BaseResponse.builder()
                    .data(response)
                    .message("Producto encontrado")
                    .success(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .data(null)
                    .message("Error al obtener el producto: " + e.getMessage())
                    .success(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @Override
    public BaseResponse getAllProducts(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> productPage = productRepository.findAll(pageable);

            List<ProductResponse> productResponses = productPage.getContent().stream()
                    .map(productMapper::toResponse)
                    .collect(Collectors.toList());

            return BaseResponse.builder()
                    .data(productResponses)
                    .message("Productos obtenidos exitosamente")
                    .success(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .data(null)
                    .message("Error al obtener los productos: " + e.getMessage())
                    .success(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @Override
    public BaseResponse searchProducts(String name, Long categoryId, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> productPage;

            if (name != null && categoryId != null) {
                productPage = productRepository.findByNameContainingAndCategoryId(name, categoryId, pageable);
            } else if (name != null) {
                productPage = productRepository.findByNameContaining(name, pageable);
            } else if (categoryId != null) {
                productPage = productRepository.findByCategoryId(categoryId, pageable);
            } else {
                productPage = productRepository.findAll(pageable);
            }

            List<ProductResponse> productResponses = productPage.getContent().stream()
                    .map(productMapper::toResponse)
                    .collect(Collectors.toList());

            return BaseResponse.builder()
                    .data(productResponses)
                    .message("Búsqueda de productos completada")
                    .success(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .data(null)
                    .message("Error en la búsqueda de productos: " + e.getMessage())
                    .success(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @Override
    public BaseResponse getProductsByCategory(Long categoryId, int page, int size) {
        try {
            // Validar que la categoría exista
            boolean categoryExists = categoryRepository.existsById(categoryId);
            if (!categoryExists) {
                return BaseResponse.builder()
                        .data(null)
                        .message("La categoría especificada no existe")
                        .success(false)
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build();
            }

            Pageable pageable = PageRequest.of(page, size);
            Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);

            List<ProductResponse> productResponses = productPage.getContent().stream()
                    .map(productMapper::toResponse)
                    .collect(Collectors.toList());

            return BaseResponse.builder()
                    .data(productResponses)
                    .message("Productos por categoría obtenidos exitosamente")
                    .success(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .data(null)
                    .message("Error al obtener productos por categoría: " + e.getMessage())
                    .success(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @Override
    @Transactional
    public BaseResponse updateProduct(Long id, ProductRequest productRequest) {
        try {
            // Verificar que el producto exista
            Optional<Product> productOpt = productRepository.findById(id);
            if (productOpt.isEmpty()) {
                return BaseResponse.builder()
                        .data(null)
                        .message("Producto no encontrado")
                        .success(false)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build();
            }

            // Verificar que la categoría exista
            Optional<Category> categoryOpt = categoryRepository.findById(productRequest.getCategoryId());
            if (categoryOpt.isEmpty()) {
                return BaseResponse.builder()
                        .data(null)
                        .message("La categoría especificada no existe")
                        .success(false)
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build();
            }

            // Actualizar el producto
            Product product = productOpt.get();
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setImageUrl(productRequest.getImageUrl());
            product.setPrice(productRequest.getPrice());
            product.setCategory(categoryOpt.get());
            product.setStock(productRequest.getStock());
            product.setSku(productRequest.getSku());

            Product updatedProduct = productRepository.save(product);
            ProductResponse response = productMapper.toResponse(updatedProduct);

            return BaseResponse.builder()
                    .data(response)
                    .message("Producto actualizado exitosamente")
                    .success(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .data(null)
                    .message("Error al actualizar el producto: " + e.getMessage())
                    .success(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @Override
    @Transactional
    public BaseResponse updateStock(Long id, Integer stock) {
        try {
            // Verificar que el producto exista
            Optional<Product> productOpt = productRepository.findById(id);
            if (productOpt.isEmpty()) {
                return BaseResponse.builder()
                        .data(null)
                        .message("Producto no encontrado")
                        .success(false)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build();
            }

            // Validar stock
            if (stock < 0) {
                return BaseResponse.builder()
                        .data(null)
                        .message("El stock no puede ser negativo")
                        .success(false)
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build();
            }

            // Actualizar stock
            Product product = productOpt.get();
            product.setStock(stock);
            Product updatedProduct = productRepository.save(product);

            ProductResponse response = productMapper.toResponse(updatedProduct);

            return BaseResponse.builder()
                    .data(response)
                    .message("Stock actualizado exitosamente")
                    .success(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .data(null)
                    .message("Error al actualizar el stock: " + e.getMessage())
                    .success(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @Override
    @Transactional
    public BaseResponse deleteProduct(Long id) {
        try {
            // Verificar que el producto exista
            boolean exists = productRepository.existsById(id);
            if (!exists) {
                return BaseResponse.builder()
                        .data(null)
                        .message("Producto no encontrado")
                        .success(false)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build();
            }

            // Eliminar producto
            productRepository.deleteById(id);

            return BaseResponse.builder()
                    .data(null)
                    .message("Producto eliminado exitosamente")
                    .success(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .data(null)
                    .message("Error al eliminar el producto: " + e.getMessage())
                    .success(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}