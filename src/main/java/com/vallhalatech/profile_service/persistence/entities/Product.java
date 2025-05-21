package com.vallhalatech.profile_service.persistence.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "product")
@Data  // Lombok para generar getters, setters, toString, equals, hashCode
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    // Usar BigDecimal para valores monetarios (precisión)
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    // Mejor como relación a tabla de categorías
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Campos adicionales útiles
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "sku", length = 20, unique = true)
    private String sku;

    // Auditoría básica
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Campos opcionales según necesidades
    @Column(name = "is_active")
    private Boolean isActive = true;

    // Métodos de ciclo de vida
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}