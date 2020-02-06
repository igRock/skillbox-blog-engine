package ru.skillbox.blog_engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.blog_engine.dto.PostRequest;
import ru.skillbox.blog_engine.dto.PostWithCommentsDto;
import ru.skillbox.blog_engine.dto.PostsResponse;
import ru.skillbox.blog_engine.dto.ResultResponse;
import ru.skillbox.blog_engine.enums.Mode;
import ru.skillbox.blog_engine.enums.ModerationStatus;
import ru.skillbox.blog_engine.enums.Status;
import ru.skillbox.blog_engine.repository.PostRepository;

import java.time.LocalDate;

@RestController
public class ApiPostController {
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/api/post/")
    public PostsResponse getPosts(@RequestParam Integer offset,
                                  @RequestParam Integer limit,
                                  @RequestParam Mode mode) {
        return null;
    }

    @GetMapping("/api/post/search/")
    public PostsResponse getPostsByQuery(@RequestParam Integer offset,
                                         @RequestParam Integer limit,
                                         @RequestParam String query) {
        return null;
    }

    @GetMapping("/api/post/{id}")
    public PostWithCommentsDto getPostById(@PathVariable String id) {
        return null;
    }

    @GetMapping("/api/post/byDate")
    public PostsResponse getPostsByDate(@RequestParam Integer offset,
                                        @RequestParam Integer limit,
                                        @RequestParam LocalDate date) {
        return null;
    }

    @GetMapping("/api/post/byTag")
    public PostsResponse getPostsByTag(@RequestParam Integer offset,
                                       @RequestParam Integer limit,
                                       @RequestParam String tag) {
        return null;
    }

    //ИЗМЕНИТЬ ФОРМАТ ОТВЕТА, ОТНАСЛЕДОВАТЬ
    @GetMapping("/api/post/moderation")
    public PostsResponse getPostsModeration(@RequestParam Integer offset,
                                            @RequestParam Integer limit,
                                            @RequestParam ModerationStatus status) {
        return null;
    }

    @GetMapping("/api/post/my")
    public PostsResponse getMyPosts(@RequestParam Integer offset,
                                    @RequestParam Integer limit,
                                    @RequestParam Status status) {
        return null;
    }

    // ФОРМАТ ОТВЕТА ПОМЕНЯТЬ
    @PostMapping("/api/post")
    public ResultResponse addNewPost(@RequestBody PostRequest request) {
        return null;
    }

    @PutMapping("/api/post/{id}")
    public ResultResponse updatePostById(@PathVariable String id,
                                         @RequestBody PostRequest request) {
        return null;
    }
}
