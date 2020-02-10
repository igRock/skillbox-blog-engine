package ru.skillbox.blog_engine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.blog_engine.dto.*;
import ru.skillbox.blog_engine.enums.SortMode;
import ru.skillbox.blog_engine.model.Post;
import ru.skillbox.blog_engine.model.PostComment;
import ru.skillbox.blog_engine.model.Tag;
import ru.skillbox.blog_engine.model.User;
import ru.skillbox.blog_engine.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.skillbox.blog_engine.enums.ModerationStatus.ACCEPTED;


@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public List<PlainPostDto> getAllPosts() {

        List<Post> postList = getPostListFromRepository();

        return filterActivePosts(postList).stream()
                .map(this::postToPlainPostDto)
                .collect(Collectors.toList());
    }

    public PostWithCommentsDto getPostWithCommentsById(Integer id) {
        Post post = postRepository.findById(id).orElse(null);
        return postToPostWithCommentsDto(post);
    }

    private List<Post> getPostListFromRepository(){
        List<Post> postList = new ArrayList<>();
        postRepository.findAll().forEach(postList::add);

        return postList;
    }

    private List<Post> filterActivePosts(List<Post> posts) {
        return posts.stream()
                .filter(post -> post.isActive() &&
                        ACCEPTED.equals(post.getModerationStatus()) &&
                        post.getTime().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
    }



    public List<PlainPostDto> searchByTag(List<PlainPostDto> list, String tag) {

        List<Post> postList = getPostListFromRepository().stream()
                .filter(post -> post.getTags().stream()
                            .map(Tag::getName)
                            .anyMatch(tag::equals))
                .collect(Collectors.toList());

        return filterActivePosts(postList).stream()
                .map(this::postToPlainPostDto)
                .collect(Collectors.toList());
    }

    public List<PlainPostDto> searchByDate(List<PlainPostDto> list, LocalDateTime date) {

        return list.stream()
                .filter(post -> post.getTime().isAfter(date) &&
                        post.getTime().isBefore(date.plusDays(1).minusSeconds(1)))
                .collect(Collectors.toList());
    }

    public List<PlainPostDto> searchByQuery(List<PlainPostDto> list, String query) {
        if (query == null || "".equals(query)) {
            return list;
        }
        return list.stream()
                .filter(post -> post.getTitle().contains(query))
//                        || post.getAnnounce().contains(query)) // Поправить, когла будет понятно,что это такое и как искать

                .collect(Collectors.toList());
    };

    public List<PlainPostDto> offsetList(Integer offset, Integer limit, List<PlainPostDto> list) {
        if (list.isEmpty() || offset > list.size()) {
            return Collections.EMPTY_LIST;
        }
        return list.subList(offset, limit + offset <= list.size() ? limit + offset : list.size());
    }

    public List<PlainPostDto> sortByMode(List<PlainPostDto> list, String mode) {
        switch (mode) {
            case "best":
                list.sort(Comparator.comparing(PlainPostDto::getLikeCount).reversed());
                break;
            case "early":
                list.sort(Comparator.comparing(PlainPostDto::getTime));
                break;
            case "recent":
                list.sort(Comparator.comparing(PlainPostDto::getTime).reversed());
                break;
            case "popular":
                list.sort(Comparator.comparing(PlainPostDto::getCommentCount).reversed());
                break;
        }
        return list;
    }








    private UserWithPhotoDto userToUserWithPhotoDto(User user) {
        UserWithPhotoDto userWithPhotoDto = new UserWithPhotoDto();
        userWithPhotoDto.setPhoto(user.getPhoto());
        userWithPhotoDto.setId(user.getId());
        userWithPhotoDto.setName(user.getName());

        return userWithPhotoDto;
    }

    private CommentDto psotCommentToCommentDto(PostComment postComment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(postComment.getId());
//        commentDto.setText(postComment.get());
        commentDto.setTime(postComment.getTime());
        commentDto.setUser(userToUserWithPhotoDto(postComment.getUser()));

        return commentDto;
    }

    private PostWithCommentsDto postToPostWithCommentsDto(Post post) {
        PostWithCommentsDto postWithCommentsDto = new PostWithCommentsDto();
        postWithCommentsDto.setId(post.getId());
        postWithCommentsDto.setText(post.getText());
        postWithCommentsDto.setTime(post.getTime());
        postWithCommentsDto.setTitle(post.getTitle());
        postWithCommentsDto.setViewCount(post.getViewCount());
        postWithCommentsDto.setUser(userToUserDto(post.getUser()));

        postWithCommentsDto.setComments(post.getPostComments().stream()
                .map(this::psotCommentToCommentDto)
                .collect(Collectors.toList()));
        postWithCommentsDto.setTags(post.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toList()));
        postWithCommentsDto.setLikeCount(post.getPostVotes().stream()
                .filter(item -> item.getValue() > 0)
                .count());
        postWithCommentsDto.setDislikeCount(post.getPostVotes().stream()
                .filter(item -> item.getValue() < 0)
                .count());

        return postWithCommentsDto;
    }

    private PlainPostDto postToPlainPostDto(Post post) {
        PlainPostDto plainPostDto = new PlainPostDto();

        plainPostDto.setCommentCount(post.getPostComments().size());
        plainPostDto.setId(post.getId());
        plainPostDto.setTitle(post.getTitle());
        plainPostDto.setViewCount(post.getViewCount());
        plainPostDto.setTime(post.getTime());
        plainPostDto.setUser(userToUserDto(post.getUser()));
        //        plainPostDto.setAnnounce(post.getText());
        plainPostDto.setDislikeCount(post.getPostVotes().stream()
                .filter(item -> item.getValue() < 0)
                .count());
        plainPostDto.setLikeCount(post.getPostVotes().stream()
                .filter(item -> item.getValue() > 0)
                .count());

        return plainPostDto;
    }

    private UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        return userDto;
    }

}
