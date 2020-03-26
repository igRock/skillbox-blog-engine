package ru.skillbox.blog_engine.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PostRequest {
    private LocalDateTime time;
    private Boolean active;
    private String title;
    private String text;
    private Set<String> tags;
}
