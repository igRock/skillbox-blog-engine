package ru.skillbox.blog_engine.dto;

public class UserAdditionalInfoDto extends UserWithPhotoDto {
    private boolean moderation;
    private Integer moderationCount;
    private boolean settings;

    public boolean isModeration() {
        return moderation;
    }

    public void setModeration(boolean moderation) {
        this.moderation = moderation;
    }

    public Integer getModerationCount() {
        return moderationCount;
    }

    public void setModerationCount(Integer moderationCount) {
        this.moderationCount = moderationCount;
    }

    public boolean isSettings() {
        return settings;
    }

    public void setSettings(boolean settings) {
        this.settings = settings;
    }
}
