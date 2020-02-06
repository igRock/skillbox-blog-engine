package ru.skillbox.blog_engine.dto;

public class CommentDto {
    private int id;
    private String time;
    private UserWithPhotoDto user;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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
