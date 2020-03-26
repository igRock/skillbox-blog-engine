package ru.skillbox.blog_engine.enums;

import lombok.Getter;
import org.springframework.core.convert.converter.Converter;

public enum PostModerationStatus {
    INACTIVE(false, ModerationStatus.NEW),
    PENDING(true, ModerationStatus.NEW),
    DECLINED(true, ModerationStatus.DECLINED),
    PUBLISHED(true, ModerationStatus.ACCEPTED);

    @Getter
    final boolean isActive;

    @Getter
    final ModerationStatus moderationStatus;

    PostModerationStatus(boolean isActive, ModerationStatus moderationStatus) {
        this.isActive = isActive;
        this.moderationStatus = moderationStatus;
    }

    public static class StringToEnumConverter implements Converter<String, PostModerationStatus> {
        @Override
        public PostModerationStatus convert(String source) {
            return PostModerationStatus.valueOf(source.toUpperCase());
        }
    }


}
