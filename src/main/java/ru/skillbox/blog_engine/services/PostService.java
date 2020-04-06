package ru.skillbox.blog_engine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.blog_engine.dto.PostRequest;
import ru.skillbox.blog_engine.dto.PostWithCommentsResponse;
import ru.skillbox.blog_engine.enums.Decision;
import ru.skillbox.blog_engine.enums.ModerationStatus;
import ru.skillbox.blog_engine.model.Post;
import ru.skillbox.blog_engine.model.Tag;
import ru.skillbox.blog_engine.model.User;
import ru.skillbox.blog_engine.repository.PostRepository;
import ru.skillbox.blog_engine.repository.TagRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    @Autowired
    private TagService tagService;

    public PostWithCommentsResponse getPostWithCommentsById(Integer id) {
        return postRepository.findById(id)
                .filter(p -> p.getIsActive() &&
                        ACCEPTED.equals(p.getModerationStatus()) &&
                        p.getTime().isBefore(LocalDateTime.now()))
                .map(entityMapper::postToPostWithCommentsDto)
                .orElse(null);
    }

    public List<Post> getAllPostsFromRepository(boolean isActive, ModerationStatus moderationStatus){
        List<Post> postList = new ArrayList<>();
        postRepository.findAll().forEach(postList::add);
        if (isActive) {
            postList = postList.stream().filter(p -> p.getIsActive() &&
                    moderationStatus.equals(p.getModerationStatus()) &&
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

    public List<Post> searchByDate(List<Post> list, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return list.stream()
                .filter(post -> post.getTime().isAfter(dateFrom) &&
                        post.getTime().isBefore(dateTo))
                .collect(Collectors.toList());
    }

    public List<Post> searchByUser(List<Post> list, User user) {
        return list.stream()
                .filter(post -> post.getUser().equals(user))
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

    public Post updatePost(Post newPost) {
        return postRepository.save(newPost);
    }

    public Post savePost(Post post, PostRequest postData, User editor) {
        final Post postToSave = (post == null) ? new Post() : post;
        final LocalDateTime NOW = LocalDateTime.now();
        postToSave.setTitle(postData.getTitle());
        postToSave.setText(postData.getText());
        postToSave.setIsActive(postData.getActive());
        if (postData.getTime() != null) {
            postToSave.setTime(postData.getTime().isBefore(NOW) ? NOW : postData.getTime());
        } else {
            postToSave.setTime(NOW);
        }
        postToSave.setUser((postToSave.getId() == null) ? editor :
                           postToSave.getUser());
        if ((post == null) || (editor.equals(postToSave.getUser()) && !editor.getIsModerator())) {
            postToSave.setModerationStatus(ModerationStatus.NEW);
        }
        if (postData.getTags() != null) {
            postData.getTags().forEach(tag -> postToSave.getTags().add(tagService.saveTag(tag)));
        }
        return postRepository.save(postToSave);
    }

    public Optional<Post> getPostById(Integer id) {
        return postRepository.findById(id);
    }

    public Post updatePostModerationStatus(User moderator, Post post, Decision decision) {
        ModerationStatus status = (decision == Decision.ACCEPT) ?
                ModerationStatus.ACCEPTED : ModerationStatus.DECLINED;
        post.setModerationStatus(status);
        post.setModeratedBy(moderator);
        return postRepository.save(post);
    }

    public Integer countByUser(User user) {
        return postRepository.countByUser(user);
    }

    public Integer countViewsByUser(User user) {
        Integer result = postRepository.getViewsByUser(user);
        result = result != null ? result : 0;
        return result;
    }

    public String getFirstPostDate(User user) {
        return postRepository.getFirstPostDateByUser(user);
    }
}
