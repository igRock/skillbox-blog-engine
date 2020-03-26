package ru.skillbox.blog_engine.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlainPostDto {
    private Integer id;
    private LocalDateTime time;
    private UserDto user;
    private String title;
    private String announce;
    private Long likeCount;
    private Long dislikeCount;
    private Integer commentCount;
    private Integer viewCount;
}
