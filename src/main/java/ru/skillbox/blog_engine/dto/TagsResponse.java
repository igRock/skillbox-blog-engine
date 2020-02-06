package ru.skillbox.blog_engine.dto;

import java.util.List;

public class TagsResponse {
    private List<TagDto> tags;

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }
}
