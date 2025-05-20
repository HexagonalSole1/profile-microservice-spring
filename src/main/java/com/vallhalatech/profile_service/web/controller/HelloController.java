package com.vallhalatech.profile_service.web.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("hello")
@Slf4j
public class HelloController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> HelloController(HttpServletRequest request) {

        log.info("🟡 ====== INCOMING REQUEST DETAILS ======");
        log.info("📍 Remote Address: {}", request.getRemoteAddr());
        log.info("📍 Remote Host: {}", request.getRemoteHost());
        log.info("📍 Method: {}", request.getMethod());
        log.info("📍 Request URL: {}", request.getRequestURL());
        log.info("📍 Request URI: {}", request.getRequestURI());
        log.info("📍 Query String: {}", request.getQueryString());
        log.info("📍 Protocol: {}", request.getProtocol());
        log.info("📍 Scheme: {}", request.getScheme());
        log.info("📍 Server Name: {}", request.getServerName());
        log.info("📍 Server Port: {}", request.getServerPort());

        // Obtener todos los headers
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        log.info("🟡 ====== REQUEST HEADERS ======");
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.put(headerName, headerValue);

            // Log especial para headers de usuario
            if (headerName.startsWith("X-User") || headerName.equals("Authorization") ||
                    headerName.startsWith("X-Gateway") || headerName.equals("X-Authenticated")) {
                log.info("🔑 {}: {}", headerName, headerValue);
            } else {
                log.info("📋 {}: {}", headerName, headerValue);
            }
        }

        // Obtener parámetros de query (si los hay)
        Map<String, String[]> parameters = request.getParameterMap();
        log.info("🟡 ====== REQUEST PARAMETERS ======");
        parameters.forEach((key, values) -> {
            log.info("📝 {}: {}", key, String.join(", ", values));
        });

        log.info("🟡 =====================================");

        // Preparar respuesta con información útil
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello World from Profile Service!");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        response.put("headers", headers);
        response.put("request_info", Map.of(
                "method", request.getMethod(),
                "uri", request.getRequestURI(),
                "remote_addr", request.getRemoteAddr(),
                "user_agent", request.getHeader("User-Agent") != null ? request.getHeader("User-Agent") : "N/A"
        ));

        // Información específica de usuario si está disponible
        if (headers.containsKey("X-User-Id")) {
            response.put("user_info", Map.of(
                    "user_id", headers.get("X-User-Id"),
                    "user_email", headers.getOrDefault("X-User-Email", "N/A"),
                    "username", headers.getOrDefault("X-Username", "N/A"),
                    "roles", headers.getOrDefault("X-User-Roles", "N/A"),
                    "authenticated", headers.getOrDefault("X-Authenticated", "false")
            ));
        }

        // Información del Gateway si está disponible
        if (headers.containsKey("X-Gateway-Origin")) {
            response.put("gateway_info", Map.of(
                    "origin", headers.get("X-Gateway-Origin"),
                    "version", headers.getOrDefault("X-Gateway-Version", "N/A"),
                    "auth_header", headers.containsKey("X-Gateway-Auth") ? "Present" : "Missing"
            ));
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint alternativo que solo retorna los headers como JSON
     */
    @GetMapping("/headers")
    public ResponseEntity<Map<String, String>> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.put(headerName, headerValue);
        }

        log.info("📋 Headers requested: {}", headers);
        return ResponseEntity.ok(headers);
    }

    /**
     * Endpoint para debuggear información específica de usuario
     */
    @GetMapping("/user-debug")
    public ResponseEntity<Map<String, Object>> debugUserInfo(HttpServletRequest request) {
        Map<String, Object> userDebug = new HashMap<>();

        // Headers de usuario
        userDebug.put("X-User-Id", request.getHeader("X-User-Id"));
        userDebug.put("X-User-Email", request.getHeader("X-User-Email"));
        userDebug.put("X-Username", request.getHeader("X-Username"));
        userDebug.put("X-User-Roles", request.getHeader("X-User-Roles"));
        userDebug.put("X-Authenticated", request.getHeader("X-Authenticated"));

        // Headers del gateway
        userDebug.put("X-Gateway-Origin", request.getHeader("X-Gateway-Origin"));
        userDebug.put("X-Gateway-Auth", request.getHeader("X-Gateway-Auth"));
        userDebug.put("X-Gateway-Version", request.getHeader("X-Gateway-Version"));

        // Authorization header
        userDebug.put("Authorization", request.getHeader("Authorization"));

        // Validaciones
        userDebug.put("validations", Map.of(
                "has_user_id", request.getHeader("X-User-Id") != null,
                "has_user_email", request.getHeader("X-User-Email") != null,
                "is_authenticated", "true".equals(request.getHeader("X-Authenticated")),
                "user_id_is_numeric", isNumeric(request.getHeader("X-User-Id")),
                "has_gateway_auth", request.getHeader("X-Gateway-Auth") != null
        ));

        log.info("🔍 User Debug Info: {}", userDebug);
        return ResponseEntity.ok(userDebug);
    }

    private boolean isNumeric(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}