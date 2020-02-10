package ru.skillbox.blog_engine.dto;

import java.time.LocalDateTime;

public class CommentDto {
    private Integer id;
    private LocalDateTime time;
    private UserWithPhotoDto user;
    private String text;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public UserWithPhotoDto getUser() {
        return user;
    }

    public void setUser(UserWithPhotoDto user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
