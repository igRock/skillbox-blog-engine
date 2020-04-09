package ru.skillbox.blog_engine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthorizeUserRequest {
    @JsonProperty(value = "e_mail")
    private String email;
    private String password;
}
