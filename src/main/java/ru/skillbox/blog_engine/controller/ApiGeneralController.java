package ru.skillbox.blog_engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.blog_engine.dto.ApiInitResponse;
import ru.skillbox.blog_engine.dto.ModerationRequest;
import ru.skillbox.blog_engine.dto.ResultResponse;
import ru.skillbox.blog_engine.dto.TagsResponse;
import ru.skillbox.blog_engine.services.ResponseService;

@RestController

public class ApiGeneralController {

    @Autowired
    private ResponseService responseService;

    @GetMapping("/api/init")
    public ApiInitResponse getApiInit() {
        ApiInitResponse response = new ApiInitResponse();
        response.setCopyright("Топчий Григорий");
        response.setCopyrightFrom("2020");
        response.setEmail("greg0piii@mail.ru");
        response.setPhone("+79056655876");
        response.setSubtitle("SubTitle");
        response.setTitle("Title");
        return response;
    }

    @PostMapping("/api/image")
    public String postImage(@RequestParam("image") MultipartFile image) {
//        String filePath = request.getServletContext().getRealPath("/");
//        multipartFile.transferTo(new File(filePath));
        return null;
    }

    @GetMapping("/api/tag")
    public ResponseEntity<TagsResponse> getTags(@RequestParam String query) {
        return new ResponseEntity<>(responseService.getTagsResponse(query), HttpStatus.OK);
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
