package com.vallhalatech.profile_service.persistence.repositories;

import com.vallhalatech.profile_service.persistence.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IProfileRepository extends JpaRepository<Profile, Long> {
    @Query("SELECT p FROM Profile p WHERE p.userId = :userId")
    Optional<Profile> findByUserId(Long userId);

    @Query("SELECT p FROM Profile p WHERE p.isPublic = true AND p.isActive = true")
    List<Profile> findPublicProfiles();

    @Query("SELECT p FROM Profile p WHERE " +
            "(p.firstName LIKE %:searchTerm% OR p.lastName LIKE %:searchTerm%) " +
            "AND p.isPublic = true AND p.isActive = true")
    List<Profile> searchProfiles(String searchTerm);

    @Query("SELECT p FROM Profile p WHERE p.location = :location AND p.isPublic = true AND p.isActive = true")
    List<Profile> findByLocation(String location);

    @Query("SELECT COUNT(p) FROM Profile p WHERE p.isActive = true")
    Long countActiveProfiles();
}
