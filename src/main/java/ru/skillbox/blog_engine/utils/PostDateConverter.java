package ru.skillbox.blog_engine.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class PostDateConverter {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static class Serialize extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator jsonGenerator, SerializerProvider provider) {
            try {
                if (value == null) {
                    jsonGenerator.writeNull();
                } else {
                    jsonGenerator.writeString(value.format(dtf));
                }
            } catch (Exception ex) {
            }
        }
    }

    public static class Deserialize extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser jsonparser, DeserializationContext context) {
            try {
                String date = jsonparser.getText();
                return (date != null) ? LocalDateTime.parse(date, dtf) : null;
            } catch (Exception ignored) {
            }
            return null;
        }
    }
}

