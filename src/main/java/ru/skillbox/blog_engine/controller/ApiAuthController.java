package ru.skillbox.blog_engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        return new ResponseEntity<>(responseService.login(authorizeUserRequest), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterUserRequest registerUserRequest) {
        return new ResponseEntity<>(responseService.registerUser(registerUserRequest), HttpStatus.OK);
    }

    @PostMapping("/password")
    public ResponseEntity<ResultResponse> resetPassword(@RequestBody PasswordResetRequest request) {
        return new ResponseEntity<>(responseService.resetUserPassword(request), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<ResultResponse> logout() {
        return new ResponseEntity<>(responseService.logout(), HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity<AuthResponse> authCheck() {
        return new ResponseEntity<>(responseService.checkUserIsAuthorized(), HttpStatus.OK);
    }

    @PostMapping("/restore")
    public ResponseEntity<ResultResponse> restore(@RequestParam String email) {
        return new ResponseEntity<>(responseService.restorePassword(email), HttpStatus.OK);
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponse> getCaptcha() {
        return new ResponseEntity<>(responseService.getCaptchaResponse(), HttpStatus.OK);
    }
}
