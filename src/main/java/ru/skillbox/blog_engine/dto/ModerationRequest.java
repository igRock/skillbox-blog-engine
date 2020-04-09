package ru.skillbox.blog_engine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.skillbox.blog_engine.enums.Decision;

@Data
public class ModerationRequest {
    @JsonProperty(value = "post_id")
    private Integer postId;
    private String decision;
}
