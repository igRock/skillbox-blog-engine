package ru.skillbox.blog_engine.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ResultResponse {
    private Boolean result;
    private Map<String, String> errors;
}
