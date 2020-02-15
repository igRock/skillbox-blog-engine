package ru.skillbox.blog_engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.blog_engine.dto.*;
import ru.skillbox.blog_engine.services.ResponseService;
import ru.skillbox.blog_engine.services.StorageService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController

public class ApiGeneralController {
    DateTimeFormatter YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy");

    @Autowired
    private ResponseService responseService;
    @Autowired
    private StorageService storageService;

    @GetMapping("/api/init")
    public ApiInitResponse getApiInit() {
        return responseService.getApiInitResponse();
    }

    @PostMapping("/api/image")
    public String postImage(@RequestParam("image") MultipartFile image) {
        return storageService.store(image);
    }

    @GetMapping("/api/tag")
    public ResponseEntity<TagsResponse> getTags(@RequestParam String query) {
        return new ResponseEntity<>(responseService.getTagsResponse(query), HttpStatus.OK);
    }

    @PostMapping("/api/moderation")
    public ResultResponse postModeration(@RequestBody ModerationRequest moderationRequest) {
        return null;
    }

    @GetMapping("/api/calendar")
    public ResponseEntity<CalendarResponse> getCalendar(@RequestParam String year) {
        return new ResponseEntity<>(responseService.getCalendarResponse(LocalDateTime.parse(year, YEAR_FORMATTER)),
                HttpStatus.OK);
    }
}
