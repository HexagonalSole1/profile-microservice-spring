package com.vallhalatech.profile_service.service.impl;


import com.vallhalatech.profile_service.persistence.entities.Profile;
import com.vallhalatech.profile_service.persistence.repositories.IProfileRepository;
import com.vallhalatech.profile_service.service.IProfileService;
import com.vallhalatech.profile_service.utils.mappers.IProfileMapper;
import com.vallhalatech.profile_service.web.DTOs.request.CreateProfileRequest;
import com.vallhalatech.profile_service.web.DTOs.request.UpdateProfileRequest;
import com.vallhalatech.profile_service.web.DTOs.response.BaseResponse;
import com.vallhalatech.profile_service.web.DTOs.response.ProfileResponse;
import com.vallhalatech.profile_service.web.DTOs.response.PublicProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements IProfileService {

    private final IProfileRepository profileRepository;
    private final IProfileMapper profileMapper;

    @Override
    public BaseResponse createProfile(Long userId, CreateProfileRequest request) {
        try {
            // Verificar si el usuario ya tiene un perfil
            Optional<Profile> existingProfile = profileRepository.findByUserId(userId);
            if (existingProfile.isPresent()) {
                return BaseResponse.error("User already has a profile");
            }

            // Crear nuevo perfil
            Profile profile = profileMapper.toEntity(request);
            profile.setUserId(userId);
            profile.setIsActive(true);

            Profile savedProfile = profileRepository.save(profile);
            ProfileResponse response = profileMapper.toProfileResponse(savedProfile);

            return BaseResponse.success(response, "Profile created successfully");

        } catch (Exception e) {
            return BaseResponse.error("Error creating profile: " + e.getMessage());
        }
    }

    @Override
    public BaseResponse updateProfile(Long userId, UpdateProfileRequest request) {
        try {
            Optional<Profile> optionalProfile = profileRepository.findByUserId(userId);
            if (optionalProfile.isEmpty()) {
                return BaseResponse.error("Profile not found");
            }

            Profile profile = optionalProfile.get();
            profileMapper.updateEntityFromRequest(request, profile);

            Profile updatedProfile = profileRepository.save(profile);
            ProfileResponse response = profileMapper.toProfileResponse(updatedProfile);

            return BaseResponse.success(response, "Profile updated successfully");

        } catch (Exception e) {
            return BaseResponse.error("Error updating profile: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse getMyProfile(Long userId) {
        try {
            Optional<Profile> optionalProfile = profileRepository.findByUserId(userId);
            if (optionalProfile.isEmpty()) {
                return BaseResponse.error("Profile not found");
            }

            Profile profile = optionalProfile.get();
            ProfileResponse response = profileMapper.toProfileResponse(profile);

            return BaseResponse.success(response, "Profile retrieved successfully");

        } catch (Exception e) {
            return BaseResponse.error("Error retrieving profile: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse getProfileById(Long profileId) {
        try {
            Optional<Profile> optionalProfile = profileRepository.findById(profileId);
            if (optionalProfile.isEmpty()) {
                return BaseResponse.error("Profile not found");
            }

            Profile profile = optionalProfile.get();

            // Solo mostrar perfil público si no es el propio usuario
            if (!profile.getIsPublic()) {
                return BaseResponse.error("Profile is private");
            }

            PublicProfileResponse response = profileMapper.toPublicProfileResponse(profile);
            return BaseResponse.success(response, "Profile retrieved successfully");

        } catch (Exception e) {
            return BaseResponse.error("Error retrieving profile: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse getPublicProfiles() {
        try {
            List<Profile> profiles = profileRepository.findPublicProfiles();
            List<PublicProfileResponse> responses = profiles.stream()
                    .map(profileMapper::toPublicProfileResponse)
                    .toList();

            return BaseResponse.success(responses, "Public profiles retrieved successfully");

        } catch (Exception e) {
            return BaseResponse.error("Error retrieving public profiles: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse searchProfiles(String searchTerm) {
        try {
            List<Profile> profiles = profileRepository.searchProfiles(searchTerm);
            List<PublicProfileResponse> responses = profiles.stream()
                    .map(profileMapper::toPublicProfileResponse)
                    .toList();

            return BaseResponse.success(responses, "Search completed successfully");

        } catch (Exception e) {
            return BaseResponse.error("Error searching profiles: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse getProfilesByLocation(String location) {
        try {
            List<Profile> profiles = profileRepository.findByLocation(location);
            List<PublicProfileResponse> responses = profiles.stream()
                    .map(profileMapper::toPublicProfileResponse)
                    .toList();

            return BaseResponse.success(responses, "Profiles by location retrieved successfully");

        } catch (Exception e) {
            return BaseResponse.error("Error retrieving profiles by location: " + e.getMessage());
        }
    }

    @Override
    public BaseResponse deleteProfile(Long userId) {
        try {
            Optional<Profile> optionalProfile = profileRepository.findByUserId(userId);
            if (optionalProfile.isEmpty()) {
                return BaseResponse.error("Profile not found");
            }

            Profile profile = optionalProfile.get();
            profile.setIsActive(false); // Soft delete

            profileRepository.save(profile);

            return BaseResponse.success(null, "Profile deleted successfully");

        } catch (Exception e) {
            return BaseResponse.error("Error deleting profile: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse getProfileStats() {
        try {
            Long activeProfiles = profileRepository.countActiveProfiles();
            Long publicProfiles = (long) profileRepository.findPublicProfiles().size();

            Map<String, Object> stats = new HashMap<>();
            stats.put("total_active_profiles", activeProfiles);
            stats.put("public_profiles", publicProfiles);
            stats.put("private_profiles", activeProfiles - publicProfiles);

            return BaseResponse.success(stats, "Profile statistics retrieved successfully");

        } catch (Exception e) {
            return BaseResponse.error("Error retrieving profile statistics: " + e.getMessage());
        }
    }
}