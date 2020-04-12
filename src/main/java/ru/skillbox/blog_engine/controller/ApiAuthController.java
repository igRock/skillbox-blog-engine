package ru.skillbox.blog_engine.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.blog_engine.dto.AuthResponse;
import ru.skillbox.blog_engine.dto.AuthorizeUserRequest;
import ru.skillbox.blog_engine.dto.CaptchaResponse;
import ru.skillbox.blog_engine.dto.PasswordResetRequest;
import ru.skillbox.blog_engine.dto.RegisterUserRequest;
import ru.skillbox.blog_engine.dto.RestorePasswordRequest;
import ru.skillbox.blog_engine.dto.ResultResponse;
import ru.skillbox.blog_engine.model.CaptchaCode;
import ru.skillbox.blog_engine.model.User;
import ru.skillbox.blog_engine.services.AuthService;
import ru.skillbox.blog_engine.services.CaptchaCodeService;
import ru.skillbox.blog_engine.services.EntityMapper;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    @Autowired
    private CaptchaCodeService captchaCodeService;
    @Autowired
    private AuthService authService;
    @Autowired
    private EntityMapper entityMapper;

    public ApiAuthController(CaptchaCodeService captchaCodeService,
                             AuthService authService, EntityMapper entityMapper) {
        this.captchaCodeService = captchaCodeService;
        this.authService = authService;
        this.entityMapper = entityMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthorizeUserRequest authorizeUserRequest) {
        AuthResponse response = new AuthResponse();
        User userFromDB = authService.loginUser(authorizeUserRequest);

        boolean result = userFromDB != null;
        response.setResult(result);
        if (result) {
            if (!authService.isValidPassword(authorizeUserRequest.getPassword(), userFromDB.getPassword())) {
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }
            response.setUser(entityMapper.getAuthorizedUserDTO(userFromDB));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterUserRequest registerUserRequest) {
        AuthResponse response = new AuthResponse();
        Map<String, Object> errors = validateUserInputAndGetErrors(registerUserRequest);
        boolean result = errors.size() == 0;
        response.setResult(result);
        if (!result) {
            response.setErrors(errors);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.setUser(entityMapper.getAuthorizedUserDTO(authService.registerUser(registerUserRequest)));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/password")
    public ResponseEntity<ResultResponse> resetPassword(@RequestBody PasswordResetRequest request) {
        ResultResponse response = new ResultResponse();
        response.setResult(authService.resetUserPassword(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<ResultResponse> logout() {
        ResultResponse response = new ResultResponse();
        authService.logoutUser();
        response.setResult(true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity<AuthResponse> authCheck() {
        if (authService.getAuthorizedUser().isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        AuthResponse response = new AuthResponse();
        Optional<User> authorizedUser = authService.getAuthorizedUser();
        authorizedUser.ifPresent(user -> response.setUser(entityMapper.getAuthorizedUserDTO(user)));
        response.setResult(authService.getAuthorizedUser().isPresent());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/restore")
    public ResponseEntity<ResultResponse> restore(@RequestBody RestorePasswordRequest request) {
        ResultResponse response = new ResultResponse();
        response.setResult(authService.restoreUserPassword(request.getEmail()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponse> getCaptcha() {
        CaptchaResponse response = new CaptchaResponse();
        CaptchaCode captchaCode = captchaCodeService.getCaptcha();
        response.setImage("data:image/png;base64, "
                              .concat(CaptchaCodeService.generateBase64Image(captchaCode.getCode())));
        response.setSecret(captchaCode.getSecretCode());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Map<String, Object> validateUserInputAndGetErrors(RegisterUserRequest user) {
        final String email = user.getEmail();

        final String password = user.getPassword();
        final String captcha = user.getCaptcha();
        final String captchaSecretCode = user.getCaptchaSecret();

        Map<String, Object> errors = new HashMap<>();
        User userFromDB = authService.findUserByEmail(email);

        if (userFromDB != null) {
            errors.put("email", "Этот адрес уже зарегистрирован.");
        }
        if (password == null || password.length() < 6) {
            errors.put("password", "Пароль короче 6 символов");
        }
        if (!captchaCodeService.isValidCaptcha(captcha, captchaSecretCode)) {
            errors.put("captcha", "Код с картинки введен неверно.");
        }
        return errors;
    }
}
