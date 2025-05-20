package com.vallhalatech.profile_service.web.DTOs.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateProfileRequest {

    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    private String avatarUrl;

    @Size(max = 200, message = "Location must not exceed 200 characters")
    private String location;

    private LocalDateTime birthDate;

    private String website;

    private Boolean isPublic;
}
