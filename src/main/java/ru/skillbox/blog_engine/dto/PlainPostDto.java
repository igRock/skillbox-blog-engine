package ru.skillbox.blog_engine.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlainPostDto {
    private Integer id;
    @JsonFormat(pattern = "hh:mm dd.MM.yyyy")
    private LocalDateTime time;
    private UserDto user;
    private String title;
    private String announce;
    private Long likeCount;
    private Long dislikeCount;
    private Integer commentCount;
    private Integer viewCount;
}
