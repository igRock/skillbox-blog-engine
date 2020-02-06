package ru.skillbox.blog_engine.dto;

public class UserWithPhotoDto extends UserDto {
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
