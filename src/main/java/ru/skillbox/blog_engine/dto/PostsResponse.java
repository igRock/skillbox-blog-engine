package ru.skillbox.blog_engine.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostsResponse {
    private Integer count;
    private List<PlainPostDto> posts;
}
