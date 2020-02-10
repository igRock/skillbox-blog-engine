package ru.skillbox.blog_engine.controller;

import org.springframework.web.bind.annotation.*;
import ru.skillbox.blog_engine.dto.*;
import ru.skillbox.blog_engine.enums.ModerationStatus;
import ru.skillbox.blog_engine.enums.Status;
import ru.skillbox.blog_engine.services.PostService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class ApiPostController {
    DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private PostService postService;

    public ApiPostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/api/post")
    public PostsResponse getPosts(@RequestParam Integer offset,
                                  @RequestParam Integer limit,
                                  @RequestParam String mode) {

        PostsResponse postsResponse = new PostsResponse();

        List<PlainPostDto> plainPostDtoList = postService.getAllPosts();

        postsResponse.setCount(plainPostDtoList.size());
        postsResponse.setPosts(postService.sortByMode(postService.offsetList(offset, limit, plainPostDtoList), mode));
        return postsResponse;
    }

    @GetMapping("/api/post/search")
    public PostsResponse getPostsByQuery(@RequestParam Integer offset,
                                         @RequestParam Integer limit,
                                         @RequestParam String query) {

        PostsResponse postsResponse = new PostsResponse();

        List<PlainPostDto> plainPostDtoList = postService.searchByQuery(postService.getAllPosts(), query);
        postsResponse.setCount(plainPostDtoList.size());
        postsResponse.setPosts(postService.offsetList(offset, limit, plainPostDtoList));

        return postsResponse;
    }

    @GetMapping("/api/post/{id}")
    public PostWithCommentsDto getPostById(@PathVariable Integer id) {
        return postService.getPostWithCommentsById(id);
    }

    @GetMapping("/api/post/byDate")
    public PostsResponse getPostsByDate(@RequestParam Integer offset,
                                        @RequestParam Integer limit,
                                        @RequestParam String date) {

        PostsResponse postsResponse = new PostsResponse();

        List<PlainPostDto> plainPostDtoList = postService.searchByDate(postService.getAllPosts(),
                LocalDateTime.parse(date, DATEFORMATTER));
        postsResponse.setCount(plainPostDtoList.size());
        postsResponse.setPosts(postService.offsetList(offset, limit, plainPostDtoList));

        return postsResponse;
    }

    @GetMapping("/api/post/byTag")
    public PostsResponse getPostsByTag(@RequestParam Integer offset,
                                       @RequestParam Integer limit,
                                       @RequestParam String tag) {

        PostsResponse postsResponse = new PostsResponse();

        List<PlainPostDto> plainPostDtoList = postService.searchByTag(postService.getAllPosts(), tag);
        postsResponse.setCount(plainPostDtoList.size());
        postsResponse.setPosts(postService.offsetList(offset, limit, plainPostDtoList));

        return postsResponse;
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
