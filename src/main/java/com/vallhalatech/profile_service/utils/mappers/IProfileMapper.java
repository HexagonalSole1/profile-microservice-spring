package com.vallhalatech.profile_service.utils.mappers;

import com.vallhalatech.profile_service.persistence.entities.Profile;

import com.vallhalatech.profile_service.web.DTOs.request.CreateProfileRequest;
import com.vallhalatech.profile_service.web.DTOs.request.UpdateProfileRequest;
import com.vallhalatech.profile_service.web.DTOs.response.ProfileResponse;
import com.vallhalatech.profile_service.web.DTOs.response.PublicProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface IProfileMapper {

    Profile toEntity(CreateProfileRequest request);

    ProfileResponse toProfileResponse(Profile profile);

    PublicProfileResponse toPublicProfileResponse(Profile profile);

    // Actualiza solo los campos no nulos del request
    void updateEntityFromRequest(UpdateProfileRequest request, @MappingTarget Profile profile);
}
