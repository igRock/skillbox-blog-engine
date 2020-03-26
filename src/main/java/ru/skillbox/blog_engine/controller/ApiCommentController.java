package ru.skillbox.blog_engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.blog_engine.dto.NewCommentRequest;
import ru.skillbox.blog_engine.dto.ResultResponse;
import ru.skillbox.blog_engine.services.ResponseService;

@RestController
@RequestMapping("/api/comment")
public class ApiCommentController {
    @Autowired
    private ResponseService responseService;

    @PostMapping("")
    public ResponseEntity<ResultResponse> addComment(@RequestBody NewCommentRequest comment) {
        return responseService.comment(comment);
    }
}
