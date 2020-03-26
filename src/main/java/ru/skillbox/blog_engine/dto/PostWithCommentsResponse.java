package ru.skillbox.blog_engine.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostWithCommentsResponse {
    private Integer id;
    private LocalDateTime time;
    private UserDto user;
    private String title;
    private String text;
    private Long likeCount;
    private Long dislikeCount;
    private Integer viewCount;
    private List<CommentDto> comments;
    private List<String> tags;
}
