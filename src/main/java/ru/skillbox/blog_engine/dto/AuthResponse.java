package ru.skillbox.blog_engine.dto;

import lombok.Data;

@Data
public class AuthResponse extends ResultResponse {
    private UserAdditionalInfoDto user;
}
