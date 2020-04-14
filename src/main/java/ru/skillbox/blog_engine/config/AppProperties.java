package ru.skillbox.blog_engine.config;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "blog-engine")
@Configuration
public class AppProperties {
    private final Map<String, Object> properties = new HashMap<>();
}
