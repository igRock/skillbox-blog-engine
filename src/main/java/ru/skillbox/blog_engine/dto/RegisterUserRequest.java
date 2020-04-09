package ru.skillbox.blog_engine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @JsonProperty(value = "e_mail")
    private String email;
    private String name;
    private String password;
    private String captcha;

    @JsonProperty(value = "captcha_secret")
    private String captchaSecret;
}
