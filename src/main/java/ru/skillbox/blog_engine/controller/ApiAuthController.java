package ru.skillbox.blog_engine.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.blog_engine.dto.AuthResponse;

@RestController
public class ApiAuthController {

    @PostMapping("/api/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestParam String email, @RequestParam String password) {
        return null;
    }

    @GetMapping("/api/auth/check")
    public ResponseEntity<AuthResponse> authCheck() {
        return null;
    }

    //ПОМЕНЯТЬ ФОРМАТ ОТВЕТА
    @PostMapping("/api/auth/restore")
    public ResponseEntity<AuthResponse> restore(@RequestParam String email) {
        return null;
    }


}
