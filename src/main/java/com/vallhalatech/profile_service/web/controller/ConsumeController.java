package com.vallhalatech.profile_service.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vallhalatech.profile_service.client.AuthServiceClient;
import com.vallhalatech.profile_service.client.dtos.BaseResponse;
import com.vallhalatech.profile_service.client.dtos.InfoUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("info")
@Slf4j  // Para logging
public class ConsumeController {

    private AuthServiceClient authServiceClient;


    private ObjectMapper objectMapper;

    @Autowired
    public ConsumeController(AuthServiceClient authServiceClient, ObjectMapper objectMapper) {
        this.authServiceClient = authServiceClient;
        this.objectMapper = objectMapper;
    }
    /**
     * 📋 Obtiene información del usuario por email
     *
     * @param email Email del usuario a buscar
     * @return Información del usuario
     */
    @GetMapping("/{email}")
    public ResponseEntity<InfoUserResponse> getUserInfo(@PathVariable String email) {

        log.info("🔍 Buscando información para email: {}", email);

        try {
            // 1. 📞 Llamar al auth-service
            ResponseEntity<BaseResponse> response = authServiceClient.findUserByEmail(email);

            // 2. ✅ Verificar respuesta HTTP exitosa
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {

                BaseResponse baseResponse = response.getBody();

                // 3. ✅ Verificar respuesta de negocio exitosa
                if (Boolean.TRUE.equals(baseResponse.getSuccess())) {

                    // 4. 🔄 Convertir datos de forma segura
                    Object data = baseResponse.getData();
                    InfoUserResponse userInfo = objectMapper.convertValue(data, InfoUserResponse.class);

                    log.info("✅ Usuario encontrado: {}", userInfo.getEmail());
                    return ResponseEntity.ok(userInfo);

                } else {
                    // Error de negocio (ej: usuario no encontrado)
                    log.warn("⚠️ Usuario no encontrado: {} - {}", email, baseResponse.getMessage());
                    return ResponseEntity.notFound().build();
                }

            } else {
                // Error HTTP
                log.error("❌ Error HTTP al buscar usuario: {} - Status: {}", email, response.getStatusCode());
                return ResponseEntity.status(response.getStatusCode()).build();
            }

        } catch (Exception e) {
            // Error general (red, timeout, etc.)
            log.error("💥 Error inesperado buscando usuario: {}", email, e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 📋 Versión alternativa que retorna solo el objeto (sin ResponseEntity)
     */
    @GetMapping("/simple/{email}")
    public InfoUserResponse getUserInfoSimple(@PathVariable String email) {

        log.info("🔍 Búsqueda simple para email: {}", email);

        try {
            ResponseEntity<BaseResponse> response = authServiceClient.findUserByEmail(email);

            if (response.getStatusCode().is2xxSuccessful() &&
                    response.getBody() != null &&
                    Boolean.TRUE.equals(response.getBody().getSuccess())) {

                Object data = response.getBody().getData();
                InfoUserResponse userInfo = objectMapper.convertValue(data, InfoUserResponse.class);

                log.info("✅ Usuario encontrado (simple): {}", userInfo.getEmail());
                return userInfo;

            } else {
                log.warn("⚠️ Usuario no encontrado (simple): {}", email);
                return null;  // O puedes lanzar una excepción personalizada
            }

        } catch (Exception e) {
            log.error("💥 Error en búsqueda simple: {}", email, e);
            throw new RuntimeException("Error obteniendo información del usuario", e);
        }
    }

    /**
     * 📋 Versión con manejo de excepciones personalizadas
     */
    @GetMapping("/detailed/{email}")
    public ResponseEntity<?> getUserInfoDetailed(@PathVariable String email) {

        log.info("🔍 Búsqueda detallada para email: {}", email);

        try {
            ResponseEntity<BaseResponse> response = authServiceClient.findUserByEmail(email);

            // Manejo detallado por código de estado
            return switch (response.getStatusCode()) {
                case OK -> {
                    BaseResponse body = response.getBody();
                    if (body != null && Boolean.TRUE.equals(body.getSuccess())) {
                        InfoUserResponse userInfo = objectMapper.convertValue(body.getData(), InfoUserResponse.class);
                        yield ResponseEntity.ok(userInfo);
                    } else {
                        yield ResponseEntity.notFound().build();
                    }
                }
                case NOT_FOUND -> {
                    yield ResponseEntity.notFound().build();
                }
                case FORBIDDEN -> {
                    yield ResponseEntity.status(FORBIDDEN)
                            .body("No tienes permiso para acceder a esta información");
                }
                case INTERNAL_SERVER_ERROR -> {
                    yield ResponseEntity.status(INTERNAL_SERVER_ERROR)
                            .body("Error interno en auth-service");
                }
                default -> {
                    yield ResponseEntity.status(response.getStatusCode())
                            .body("Error inesperado: " + response.getStatusCode());
                }
            };

        } catch (Exception e) {
            log.error("💥 Error en búsqueda detallada: {}", email, e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
        }
    }
}


