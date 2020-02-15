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
public class ApiPostController {
    DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private ResponseService responseService;

    @GetMapping("/api/post")
    public ResponseEntity<PostsResponse> getPosts(@RequestParam Integer offset,
                                  @RequestParam Integer limit,
                                  @RequestParam String mode) {
        return new ResponseEntity<>(responseService.getPostsResponse(offset, limit, mode,
                null, null, null), HttpStatus.OK);
    }

    @GetMapping("/api/post/search")
    public ResponseEntity<PostsResponse> getPostsByQuery(@RequestParam Integer offset,
                                         @RequestParam Integer limit,
                                         @RequestParam String query) {
        return new ResponseEntity<>(responseService.getPostsResponse(offset, limit, null, query,
                null, null), HttpStatus.OK);
    }

    @GetMapping("/api/post/{id}")
    public ResponseEntity<PostWithCommentsResponse> getPostById(@PathVariable Integer id) {
        return new ResponseEntity<>(responseService.getPostWithCommentsResponse(id), HttpStatus.OK);
    }

    @GetMapping("/api/post/byDate")
    public ResponseEntity<PostsResponse> getPostsByDate(@RequestParam Integer offset,
                                        @RequestParam Integer limit,
                                        @RequestParam String date) {
        return new ResponseEntity<>(responseService.getPostsResponse(offset, limit, null,
                null, LocalDateTime.parse(date, DATEFORMATTER), null), HttpStatus.OK);
    }

    @GetMapping("/api/post/byTag")
    public ResponseEntity<PostsResponse> getPostsByTag(@RequestParam Integer offset,
                                       @RequestParam Integer limit,
                                       @RequestParam String tag) {
        return new ResponseEntity<>(responseService.getPostsResponse(offset, limit, null,
                null, null, tag), HttpStatus.OK);
    }

    //ИЗМЕНИТЬ ФОРМАТ ОТВЕТА, ОТНАСЛЕДОВАТЬ
    @GetMapping("/api/post/moderation")
    public ResponseEntity<PostsResponse> getPostsModeration(@RequestParam Integer offset,
                                            @RequestParam Integer limit,
                                            @RequestParam ModerationStatus status) {
        return null;
    }

    @GetMapping("/api/post/my")
    public ResponseEntity<PostsResponse> getMyPosts(@RequestParam Integer offset,
                                    @RequestParam Integer limit,
                                    @RequestParam Status status) {
        return null;
    }

    // ФОРМАТ ОТВЕТА ПОМЕНЯТЬ
    @PostMapping("/api/post")
    public ResponseEntity<ResultResponse> addNewPost(@RequestBody PostRequest request) {
        return null;
    }

    @PutMapping("/api/post/{id}")
    public ResponseEntity<ResultResponse> updatePostById(@PathVariable String id,
                                         @RequestBody PostRequest request) {
        return null;
    }
}
