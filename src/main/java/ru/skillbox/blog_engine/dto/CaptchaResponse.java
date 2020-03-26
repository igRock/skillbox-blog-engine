package ru.skillbox.blog_engine.dto;

import lombok.Data;

@Data
public class CaptchaResponse {
    private String secret;
    private String image;
}
