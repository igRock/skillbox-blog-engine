package ru.skillbox.blog_engine.dto;

import lombok.Data;

@Data
public class RestorePasswordRequest {
    private String email;
}
