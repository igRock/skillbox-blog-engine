package ru.skillbox.blog_engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.blog_engine.dto.*;
import ru.skillbox.blog_engine.services.ResponseService;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    @Autowired
    private ResponseService responseService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthorizeUserRequest authorizeUserRequest) {
        return responseService.login(authorizeUserRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterUserRequest registerUserRequest) {
        return responseService.registerUser(registerUserRequest);
    }

    @PostMapping("/password")
    public ResponseEntity<ResultResponse> resetPassword(@RequestBody PasswordResetRequest request) {
        return responseService.resetUserPassword(request);
    }

    @GetMapping("/logout")
    public ResponseEntity<ResultResponse> logout() {
        return responseService.logout();
    }

    @GetMapping("/check")
    public ResponseEntity<AuthResponse> authCheck() {
        return responseService.getAuthorizedUserResponse();
    }

    @PostMapping("/restore")
    public ResponseEntity<ResultResponse> restore(@RequestParam String email) {
        return responseService.restorePassword(email);
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponse> getCaptcha() {
        return responseService.getCaptchaResponse();
    }
}
