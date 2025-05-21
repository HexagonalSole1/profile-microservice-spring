package com.vallhalatech.profile_service.persistence.repositories;

import com.vallhalatech.profile_service.persistence.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface IProductRepository  extends JpaRepository<Product, Long> {
    Page<Product> findByNameContaining(String name, Pageable pageable);
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Product> findByNameContainingAndCategoryId(String name, Long categoryId, Pageable pageable);
    boolean existsByCategoryId(Long categoryId);

}
