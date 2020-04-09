package ru.skillbox.blog_engine.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class PostRequest {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime time;
    private Boolean active;
    private String title;
    private String text;
    private Set<String> tags;
}
