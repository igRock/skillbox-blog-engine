package ru.skillbox.blog_engine.dto;

import lombok.Data;

@Data
public class ApiInitResponse {
    private String title;
    private String subtitle;
    private String phone;
    private String email;
    private String copyright;
    private String copyrightFrom;
}
