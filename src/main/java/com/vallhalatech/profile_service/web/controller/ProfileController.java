package com.vallhalatech.profile_service.web.controller;


import com.vallhalatech.profile_service.service.IProfileService;
import com.vallhalatech.profile_service.web.DTOs.request.CreateProfileRequest;
import com.vallhalatech.profile_service.web.DTOs.request.UpdateProfileRequest;
import com.vallhalatech.profile_service.web.DTOs.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final IProfileService profileService;

    /**
     * Crear un nuevo perfil para el usuario autenticado
     */
    @PostMapping
    public ResponseEntity<BaseResponse> createProfile(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateProfileRequest request) {

        BaseResponse response = profileService.createProfile(userId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualizar el perfil del usuario autenticado
     */
    @PutMapping
    public ResponseEntity<BaseResponse> updateProfile(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody UpdateProfileRequest request) {

        BaseResponse response = profileService.updateProfile(userId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener el perfil del usuario autenticado
     */
    @GetMapping("/me")
    public ResponseEntity<BaseResponse> getMyProfile(
            @RequestHeader("X-User-Id") Long userId) {

        BaseResponse response = profileService.getMyProfile(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener un perfil público por ID
     */
    @GetMapping("/{profileId}")
    public ResponseEntity<BaseResponse> getProfileById(@PathVariable Long profileId) {
        BaseResponse response = profileService.getProfileById(profileId);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener todos los perfiles públicos
     */
    @GetMapping("/public")
    public ResponseEntity<BaseResponse> getPublicProfiles() {
        BaseResponse response = profileService.getPublicProfiles();
        return ResponseEntity.ok(response);
    }

    /**
     * Buscar perfiles públicos por nombre
     */
    @GetMapping("/search")
    public ResponseEntity<BaseResponse> searchProfiles(@RequestParam String q) {
        BaseResponse response = profileService.searchProfiles(q);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener perfiles por ubicación
     */
    @GetMapping("/location/{location}")
    public ResponseEntity<BaseResponse> getProfilesByLocation(@PathVariable String location) {
        BaseResponse response = profileService.getProfilesByLocation(location);
        return ResponseEntity.ok(response);
    }

    /**
     * Eliminar (desactivar) el perfil del usuario autenticado
     */
    @DeleteMapping
    public ResponseEntity<BaseResponse> deleteProfile(
            @RequestHeader("X-User-Id") Long userId) {

        BaseResponse response = profileService.deleteProfile(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener estadísticas de perfiles (solo para administradores)
     */
    @GetMapping("/stats")
    public ResponseEntity<BaseResponse> getProfileStats() {
        BaseResponse response = profileService.getProfileStats();
        return ResponseEntity.ok(response);
    }
}