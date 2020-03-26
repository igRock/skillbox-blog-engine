package ru.skillbox.blog_engine.dto;

import lombok.Data;

@Data
public class UserWithPhotoDto extends UserDto {
    private String photo;
}
