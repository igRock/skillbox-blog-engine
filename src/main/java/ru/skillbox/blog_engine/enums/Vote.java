package ru.skillbox.blog_engine.enums;

import org.springframework.core.convert.converter.Converter;

public enum Vote {
    LIKE,
    DISLIKE;

    public static class StringToEnumConverter implements Converter<String, Vote> {
        @Override
        public Vote convert(String s) {
            return Vote.valueOf(s.toUpperCase());
        }
    }
}
