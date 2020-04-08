package ru.skillbox.blog_engine.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.blog_engine.dto.PostRequest;
import ru.skillbox.blog_engine.dto.PostsResponse;
import ru.skillbox.blog_engine.dto.ResultResponse;
import ru.skillbox.blog_engine.enums.ModerationStatus;
import ru.skillbox.blog_engine.enums.PostModerationStatus;
import ru.skillbox.blog_engine.enums.SortMode;
import ru.skillbox.blog_engine.enums.Vote;
import ru.skillbox.blog_engine.services.PostService;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    @Autowired
    private PostService postService;

    public ApiPostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("")
    public ResponseEntity<PostsResponse> getPosts(
            @RequestParam Integer offset,
            @RequestParam Integer limit,
            @RequestParam SortMode mode) {
        return postService.getPostsResponse(offset, limit, mode, null, null,
                null, null, null, true);
    }

    @GetMapping("/search")
    public ResponseEntity<PostsResponse> getPostsByQuery(
            @RequestParam Integer offset,
            @RequestParam Integer limit,
            @RequestParam String query) {
        return postService.getPostsResponse(offset, limit, null, query, null,
                null, null, null, true);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Integer id) {
        return postService.getPostWithCommentsResponse(id);
    }

    @GetMapping("/byDate")
    public ResponseEntity<PostsResponse> getPostsByDate(
            @RequestParam Integer offset,
            @RequestParam Integer limit,
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
        return postService.getPostsResponse(offset, limit, null, null,
                                                LocalDateTime.of(date, LocalTime.MIDNIGHT), null, null, null, true);
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostsResponse> getPostsByTag(
            @RequestParam Integer offset,
            @RequestParam Integer limit,
            @RequestParam String tag) {
        return postService.getPostsResponse(offset, limit, null, null,
                null, tag, null, null, true);
    }

    @GetMapping("/my")
    public ResponseEntity<PostsResponse> getMyPosts(
            @RequestParam Integer offset,
            @RequestParam Integer limit,
            @RequestParam PostModerationStatus status) {
        status = (status == null) ? PostModerationStatus.INACTIVE : status;
        return postService.getMyPosts(offset, limit, status);
    }

    @GetMapping("/moderation")
    public ResponseEntity<PostsResponse> getPostsModeration(
            @RequestParam Integer offset,
            @RequestParam Integer limit,
            @RequestParam ModerationStatus status) {
        return postService.getModeratedPosts(offset, limit, status);
    }

    @PostMapping("")
    public ResponseEntity<?> addNewPost(@RequestBody PostRequest request) {
        return postService.addNewPost(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editPost(
            @PathVariable Integer id,
            @RequestBody PostRequest request) {
        return postService.editPost(id, request);
    }

    @PostMapping("/{vote}")
    public ResponseEntity<ResultResponse> votePost(@PathVariable Vote vote, @RequestBody Map<String, Integer> body) {
        return postService.votePost(vote,  body.getOrDefault("post_id", 0));
    }

}
