package com.vallhalatech.profile_service.web.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class HelloController {

    @GetMapping
    public ResponseEntity<String> HelloController() {

        return ResponseEntity.ok("Hello World");

    }

}
