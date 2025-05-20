package com.vallhalatech.profile_service.service;



import com.vallhalatech.profile_service.web.DTOs.request.CreateProfileRequest;
import com.vallhalatech.profile_service.web.DTOs.request.UpdateProfileRequest;
import com.vallhalatech.profile_service.web.DTOs.response.BaseResponse;

import java.util.List;

public interface IProfileService {

    BaseResponse createProfile(Long userId, CreateProfileRequest request);

    BaseResponse updateProfile(Long userId, UpdateProfileRequest request);

    BaseResponse getMyProfile(Long userId);

    BaseResponse getProfileById(Long profileId);

    BaseResponse getPublicProfiles();

    BaseResponse searchProfiles(String searchTerm);

    BaseResponse getProfilesByLocation(String location);

    BaseResponse deleteProfile(Long userId);

    BaseResponse getProfileStats();
}