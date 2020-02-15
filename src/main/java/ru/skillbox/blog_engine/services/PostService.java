package ru.skillbox.blog_engine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.blog_engine.dto.PostWithCommentsResponse;
import ru.skillbox.blog_engine.model.Post;
import ru.skillbox.blog_engine.model.Tag;
import ru.skillbox.blog_engine.repository.PostRepository;
import ru.skillbox.blog_engine.repository.TagRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.skillbox.blog_engine.enums.ModerationStatus.ACCEPTED;


@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private EntityMapper entityMapper;

    public PostWithCommentsResponse getPostWithCommentsById(Integer id) {
        Post post = postRepository.findById(id)
                .filter(p -> p.isActive() &&
                        ACCEPTED.equals(p.getModerationStatus()) &&
                        p.getTime().isBefore(LocalDateTime.now()))
                .orElse(null);
        return entityMapper.postToPostWithCommentsDto(post);
    }

    public List<Post> getAllPostsFromRepository(boolean isActive){
        List<Post> postList = new ArrayList<>();
        postRepository.findAll().forEach(postList::add);
        if (isActive) {
            postList = postList.stream().filter(p -> p.isActive() &&
                    ACCEPTED.equals(p.getModerationStatus()) &&
                    p.getTime().isBefore(LocalDateTime.now()))
                    .collect(Collectors.toList());
        }
        return postList;
    }

    public List<Post> searchByTag(List<Post> postList, String tagQuery) {
        return postList.stream()
                .filter(post -> post.getTags().stream()
                        .map(Tag::getName)
                        .anyMatch(tag -> tag.toLowerCase().startsWith(tagQuery.toLowerCase())))
                .collect(Collectors.toList());
    }

    public List<Post> searchByDate(List<Post> list, LocalDateTime date) {
        return list.stream()
                .filter(post -> post.getTime().isAfter(date) &&
                        post.getTime().isBefore(date.plusDays(1).minusSeconds(1)))
                .collect(Collectors.toList());
    }

    public List<Post> searchByQuery(List<Post> list, String query) {
        if (query == null || "".equals(query)) {
            return list;
        }
        return list.stream()
                .filter(post -> post.getTitle().contains(query) || post.getText().contains(query))
                .collect(Collectors.toList());
    }
}
