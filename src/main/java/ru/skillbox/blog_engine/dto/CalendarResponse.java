package ru.skillbox.blog_engine.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CalendarResponse {
    private List<Integer> years;
    private Map<String, Long> posts;
}
