package ru.skillbox.blog_engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.blog_engine.dto.PostRequest;
import ru.skillbox.blog_engine.dto.PostWithCommentsResponse;
import ru.skillbox.blog_engine.dto.PostsResponse;
import ru.skillbox.blog_engine.dto.ResultResponse;
import ru.skillbox.blog_engine.enums.ModerationStatus;
import ru.skillbox.blog_engine.enums.Status;
import ru.skillbox.blog_engine.services.ResponseService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {
    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private ResponseService responseService;

    @GetMapping("")
    public ResponseEntity<PostsResponse> getPosts(@RequestParam Integer offset,
                                  @RequestParam Integer limit,
                                  @RequestParam String mode) {
        return new ResponseEntity<>(responseService.getPostsResponse(offset, limit, mode,
                null, null, null), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<PostsResponse> getPostsByQuery(@RequestParam Integer offset,
                                         @RequestParam Integer limit,
                                         @RequestParam String query) throws IllegalAccessException{
        return new ResponseEntity<>(responseService.getPostsResponse(offset, limit, null, query,
                null, null), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostWithCommentsResponse> getPostById(@PathVariable Integer id) {
        return new ResponseEntity<>(responseService.getPostWithCommentsResponse(id), HttpStatus.OK);
    }

    @GetMapping("/byDate")
    public ResponseEntity<PostsResponse> getPostsByDate(@RequestParam Integer offset,
                                        @RequestParam Integer limit,
                                        @RequestParam String date) {
        return new ResponseEntity<>(responseService.getPostsResponse(offset, limit, null,
                null, LocalDateTime.parse(date, DATE_FORMATTER), null), HttpStatus.OK);
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostsResponse> getPostsByTag(@RequestParam Integer offset,
                                       @RequestParam Integer limit,
                                       @RequestParam String tag) {
        return new ResponseEntity<>(responseService.getPostsResponse(offset, limit, null,
                null, null, tag), HttpStatus.OK);
    }

    //ИЗМЕНИТЬ ФОРМАТ ОТВЕТА, ОТНАСЛЕДОВАТЬ
    @GetMapping("/moderation")
    public ResponseEntity<PostsResponse> getPostsModeration(@RequestParam Integer offset,
                                            @RequestParam Integer limit,
                                            @RequestParam ModerationStatus status) {
        return null;
    }

    @GetMapping("/my")
    public ResponseEntity<PostsResponse> getMyPosts(@RequestParam Integer offset,
                                    @RequestParam Integer limit,
                                    @RequestParam Status status) {
        return null;
    }

    @PostMapping("")
    public ResponseEntity<ResultResponse> addNewPost(@RequestBody PostRequest request) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse> updatePostById(@PathVariable String id,
                                         @RequestBody PostRequest request) {
        return null;
    }
}
