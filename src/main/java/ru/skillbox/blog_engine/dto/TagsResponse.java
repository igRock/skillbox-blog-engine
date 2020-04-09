package ru.skillbox.blog_engine.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagsResponse {
    private List<TagDto> tags;
}
