package com.vallhalatech.profile_service.web.controller;

import com.vallhalatech.profile_service.persistence.entities.Product;
import com.vallhalatech.profile_service.service.IProductService;

import com.vallhalatech.profile_service.web.dtos.product.request.*;
import com.vallhalatech.profile_service.web.dtos.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final IProductService productService;

    @Autowired
    public ProductController( IProductService productService) {
        this.productService = productService;
    }

    // Crear un nuevo producto
    @PostMapping
    public ResponseEntity<BaseResponse> addProduct(@Valid @RequestBody ProductRequest productRequest) {
        BaseResponse response = productService.addProduct(productRequest);
        return response.buildResponseEntity();
    }

    // Obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getProductById(@PathVariable Long id) {
        BaseResponse response = productService.getProductById(id);
        return response.buildResponseEntity();
    }

    // Obtener todos los productos (opcional: con paginación)
    @GetMapping
    public ResponseEntity<BaseResponse> getAllProducts(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        BaseResponse response = productService.getAllProducts(page, size);
        return response.buildResponseEntity();
    }

    // Filtrar productos por nombre
    @GetMapping("/search")
    public ResponseEntity<BaseResponse> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        BaseResponse response = productService.searchProducts(name, categoryId, page, size);
        return response.buildResponseEntity();
    }

    // Obtener productos por categoría
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<BaseResponse> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        BaseResponse response = productService.getProductsByCategory(categoryId, page, size);
        return response.buildResponseEntity();
    }

    // Actualizar un producto
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest productRequest) {
        BaseResponse response = productService.updateProduct(id, productRequest);
        return response.buildResponseEntity();
    }

    // Actualizar solo el stock (control de inventario)
    @PatchMapping("/{id}/stock")
    public ResponseEntity<BaseResponse> updateStock(
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateRequest stockRequest) {
        BaseResponse response = productService.updateStock(id, stockRequest.getStock());
        return response.buildResponseEntity();
    }

    // Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteProduct(@PathVariable Long id) {
        BaseResponse response = productService.deleteProduct(id);
        return response.buildResponseEntity();
    }
}