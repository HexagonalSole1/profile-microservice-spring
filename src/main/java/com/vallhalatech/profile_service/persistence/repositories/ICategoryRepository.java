package com.vallhalatech.profile_service.persistence.repositories;

import com.vallhalatech.profile_service.persistence.entities.Category;
import com.vallhalatech.profile_service.persistence.entities.Product;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.Optional;

public interface ICategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    Optional<Category> findByName(String name);
}
