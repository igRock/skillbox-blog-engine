package ru.skillbox.blog_engine.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.skillbox.blog_engine.enums.Decision;
import ru.skillbox.blog_engine.enums.ModerationStatus;
import ru.skillbox.blog_engine.enums.PostModerationStatus;
import ru.skillbox.blog_engine.enums.SortMode;
import ru.skillbox.blog_engine.enums.StatisticsType;
import ru.skillbox.blog_engine.enums.Vote;

@Configuration
@ComponentScan("ru.skillbox.blog_engine.controller")
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ModerationStatus.StringToEnumConverter());
        registry.addConverter(new PostModerationStatus.StringToEnumConverter());
        registry.addConverter(new SortMode.StringToEnumConverter());
        registry.addConverter(new Decision.StringToEnumConverter());
        registry.addConverter(new Vote.StringToEnumConverter());
        registry.addConverter(new StatisticsType.StringToEnumConverter());
    }
}
