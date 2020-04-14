package ru.skillbox.blog_engine.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.core.convert.converter.Converter;

public enum Decision {
    @JsonProperty("decline")
    DECLINE,
    @JsonProperty("accept")
    ACCEPT;

    private static final Decision[] values = Decision.values();

    public static Decision getById(int id) {
        return values[id];
    }

    public static class StringToEnumConverter implements Converter<String, Decision> {
        @Override
        public Decision convert(String source) {
            return Decision.valueOf(source.toUpperCase());
        }
    }
}
