package ru.skillbox.blog_engine.controller;

import org.springframework.web.bind.annotation.*;
import ru.skillbox.blog_engine.dto.ModerationRequest;
import ru.skillbox.blog_engine.dto.ResultResponse;

import java.io.File;

@RestController
public class ApiGeneralController {


    // ИЗМЕНИТЬ ТЕЛО ЗАПРОСА]
    @PostMapping("/api/image")
    public String postImage(@RequestBody File image) {
        return null;
    }

    @GetMapping("/api/tag/")
    public String getTags(@RequestParam String query) {
        return null;
    }

    @PostMapping("/api/moderation")
    public ResultResponse postModeration(@RequestBody ModerationRequest moderationRequest) {
        return null;
    }

    // ЗАМЕНИТЬ ФОРМАТ ОТВЕТА
    @GetMapping("/api/calendar/")
    public String getCalendar(@RequestParam String year) {
        return null;
    }
}
