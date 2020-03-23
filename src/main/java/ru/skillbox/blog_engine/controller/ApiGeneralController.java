package ru.skillbox.blog_engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.blog_engine.dto.*;
import ru.skillbox.blog_engine.services.AuthService;
import ru.skillbox.blog_engine.services.ResponseService;
import ru.skillbox.blog_engine.services.StorageService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {
    DateTimeFormatter YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy");

    @Autowired
    private ResponseService responseService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private AuthService authService;

    @GetMapping("/init")
    public ApiInitResponse getApiInit() throws IllegalAccessException {
        return responseService.getApiInitResponse();
    }

    @PostMapping("/image")
    public ResponseEntity<String> postImage(@RequestParam("image") MultipartFile image) {
        return new ResponseEntity<>(storageService.store(image), HttpStatus.OK);
    }

    @GetMapping("/tag")
    public ResponseEntity<TagsResponse> getTags(@RequestParam(required = false) String query) {
        return new ResponseEntity<>(responseService.getTagsResponse(query), HttpStatus.OK);
    }

    @PostMapping("/moderation")
    public ResultResponse postModeration(@RequestBody ModerationRequest moderationRequest) {
        return null;
    }

    @GetMapping("/calendar")
    public ResponseEntity<CalendarResponse> getCalendar(@RequestParam String year){
//        authService.getAuthorizedUser().orElseThrow(IllegalAccessError::new);
        return new ResponseEntity<>(responseService.getCalendarResponse(LocalDateTime.of(Integer.parseInt(year), 1,1,0,0)),
                HttpStatus.OK);
    }
}
