package ru.skillbox.blog_engine.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import ru.skillbox.blog_engine.utils.PostDateConverter;

@Data
public class PostRequest {
    @JsonSerialize(using = PostDateConverter.Serialize.class)
    @JsonDeserialize(using = PostDateConverter.Deserialize.class)
    private LocalDateTime time;
    private Boolean active;
    private String title;
    private String text;
    private Set<String> tags;
}
