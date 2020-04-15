package ru.skillbox.blog_engine.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.blog_engine.dto.PlainPostDto;
import ru.skillbox.blog_engine.dto.PostRequest;
import ru.skillbox.blog_engine.dto.PostWithCommentsResponse;
import ru.skillbox.blog_engine.dto.PostsResponse;
import ru.skillbox.blog_engine.dto.ResultResponse;
import ru.skillbox.blog_engine.enums.ModerationStatus;
import ru.skillbox.blog_engine.enums.PostModerationStatus;
import ru.skillbox.blog_engine.enums.SortMode;
import ru.skillbox.blog_engine.enums.Vote;
import ru.skillbox.blog_engine.model.Post;
import ru.skillbox.blog_engine.model.Tag;
import ru.skillbox.blog_engine.model.User;
import ru.skillbox.blog_engine.repository.PostRepository;
import ru.skillbox.blog_engine.repository.TagRepository;


@Service
public class PostService {
    @Autowired
    private TagService tagService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private AuthService authService;
    @Autowired
    private VotesService votesService;

    public PostService(PostRepository postRepository,
                       EntityMapper entityMapper,
                       AuthService authService, VotesService votesService) {
        this.postRepository = postRepository;
        this.entityMapper = entityMapper;
        this.authService = authService;
        this.votesService = votesService;
    }

    public ResponseEntity<PostsResponse> getPostsResponse(Integer offset,
                                                          Integer limit,
                                                          SortMode sortMode,
                                                          String searchQuery,
                                                          LocalDateTime ldt,
                                                          String tag,
                                                          User user,
                                                          ModerationStatus status,
                                                          Boolean isActive) {
        status = status == null ? ModerationStatus.ACCEPTED : status;
        List<Post> postList = getAllPostsFromRepository(isActive, status);
        if (searchQuery != null) {
            postList = searchByQuery(postList, searchQuery);
        }
        if (ldt != null) {
            LocalDateTime dateTo = ldt.plusDays(1).minusSeconds(1);
            postList = searchByDate(postList, ldt, dateTo);
        }
        if (tag != null) {
            postList = searchByTag(postList, tag);
        }
        if (user != null) {
            postList = searchByUser(postList, user);
        }
        List<PlainPostDto> plainPostDtoList = postList.stream()
            .map(entityMapper::postToPlainPostDto)
            .collect(Collectors.toList());
        if (sortMode != null) {
            plainPostDtoList = sortPlainPostDtoListByMode(plainPostDtoList, sortMode);
        }
        return new ResponseEntity<>(formPostsResponse(offset, limit, plainPostDtoList), HttpStatus.OK);
    }

    public ResponseEntity<?> getPostWithCommentsResponse(Integer id) {
        Optional<Post> postOptional = getPostById(id);

        if (postOptional.isEmpty())
            return new ResponseEntity<>(String.format("Пост с идентификатором '%d' не найден!", id),
                                        HttpStatus.NOT_FOUND);

        Post post = postOptional.get();
        PostWithCommentsResponse result = entityMapper.postToPostWithCommentsDto(post);

        post.incrementViewCount();
        postRepository.save(post);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<PostsResponse> getMyPosts(Integer offset, Integer limit, PostModerationStatus status) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User authorizedUser = userOptional.get();
        status = status == null ? PostModerationStatus.PUBLISHED : status;
        return getPostsResponse(offset, limit, null, null, null,
                                null, authorizedUser, status.getModerationStatus(), status.isActive());
    }

    public ResponseEntity<PostsResponse> getModeratedPosts(Integer offset, Integer limit, ModerationStatus status) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User authorizedUser = userOptional.get();
        if (!authorizedUser.getIsModerator()) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        return getPostsResponse(offset, limit, null, null, null,
                                null, null, status, true);
    }

    public ResponseEntity<ResultResponse> addNewPost(PostRequest request) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User user = userOptional.get();
        Post savedPost = savePost(null, request, user);
        ResultResponse result = new ResultResponse();
        result.setResult(savedPost != null);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<ResultResponse> editPost(Integer id, PostRequest request) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User user = userOptional.get();
        Optional<Post> postOptional = getPostById(id);
        if (postOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Post post = postOptional.get();
        if (!post.getUser().equals(user) &&
            (!user.getIsModerator() || (post.getModeratedBy() != null &&
                !post.getModeratedBy().equals(user)))) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        Post savedPost = savePost(post, request, user);
        ResultResponse result = new ResultResponse();
        result.setResult(savedPost != null);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<ResultResponse> votePost(Vote vote, Integer postId) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User user = userOptional.get();
        if (postId <= 0) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Post post = getPostById(postId).orElse(null);
        if (post == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        ResultResponse result = new ResultResponse();
        result.setResult(votesService.vote(vote, user, post));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private PostsResponse formPostsResponse(Integer offset, Integer limit, List<PlainPostDto> posts) {
        PostsResponse postsResponse = new PostsResponse();
        postsResponse.setCount(posts.size());
        postsResponse.setPosts(offsetList(offset, limit, posts));
        return postsResponse;
    }

    private List<PlainPostDto> sortPlainPostDtoListByMode(List<PlainPostDto> list, SortMode mode) {
        switch (mode) {
            case BEST:
                list.sort(Comparator.comparing(PlainPostDto::getLikeCount).reversed());
                break;
            case EARLY:
                list.sort(Comparator.comparing(PlainPostDto::getTime));
                break;
            case RECENT:
                list.sort(Comparator.comparing(PlainPostDto::getTime).reversed());
                break;
            case POPULAR:
                list.sort(Comparator.comparing(PlainPostDto::getCommentCount).reversed());
                break;
        }
        return list;
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

    public List<Post> searchByDate(List<Post> list, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return list.stream()
            .filter(post -> post.getTime().isAfter(dateFrom) &&
                post.getTime().isBefore(dateTo.plusHours(3)))
            .collect(Collectors.toList());
    }

    public Optional<Post> getPostById(Integer id) {
        return postRepository.findById(id);
    }

    public Post savePost(Post newPost) {
        return postRepository.save(newPost);
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

    private <T> List offsetList(Integer offset, Integer limit, List<T> list) {
        if (list.isEmpty() || offset > list.size()) {
            return Collections.EMPTY_LIST;
        }
        return list.subList(offset, limit + offset <= list.size() ? limit + offset : list.size());
    }

    private List<Post> searchByTag(List<Post> postList, String tagQuery) {
        return postList.stream()
                .filter(post -> post.getTags().stream()
                        .map(Tag::getName)
                        .anyMatch(tag -> tag.toLowerCase().startsWith(tagQuery.toLowerCase())))
                .collect(Collectors.toList());
    }

    private List<Post> searchByUser(List<Post> list, User user) {
        return list.stream()
                .filter(post -> post.getUser().equals(user))
                .collect(Collectors.toList());
    }

    private List<Post> searchByQuery(List<Post> list, String query) {
        if (query == null || "".equals(query)) {
            return list;
        }
        return list.stream()
                .filter(post -> post.getTitle().contains(query) || post.getText().contains(query))
                .collect(Collectors.toList());
    }

    private Post savePost(Post post, PostRequest postData, User editor) {
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
}
