package ru.skillbox.blog_engine.dto;

import java.util.List;

public class PostsResponse {
    private int count;
    private List<PlainPostDto> posts;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PlainPostDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PlainPostDto> posts) {
        this.posts = posts;
    }
}
