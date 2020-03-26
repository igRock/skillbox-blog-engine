package ru.skillbox.blog_engine.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Integer id;
    private LocalDateTime time;
    private UserWithPhotoDto user;
    private String text;
}
