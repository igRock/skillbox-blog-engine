package ru.skillbox.blog_engine.dto;

import lombok.Data;

@Data
public class UserAdditionalInfoDto extends UserWithPhotoDto {
    private boolean moderation;
    private Integer moderationCount;
    private boolean settings;
    private String email;
}
