package com.vallhalatech.profile_service.web.DTOs.response;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PublicProfileResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String bio;
    private String avatarUrl;
    private String location;
    private String website;
    private LocalDateTime createdAt;

    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        }
        return null;
    }
}