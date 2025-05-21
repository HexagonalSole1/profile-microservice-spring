package com.vallhalatech.profile_service.persistence.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "category")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50, unique = true)
    private String name;

    @Column(name = "description", length = 200)
    private String description;
}