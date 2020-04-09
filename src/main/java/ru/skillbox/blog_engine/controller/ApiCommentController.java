package ru.skillbox.blog_engine.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.blog_engine.dto.CommentResponse;
import ru.skillbox.blog_engine.dto.NewCommentRequest;
import ru.skillbox.blog_engine.dto.ResultResponse;
import ru.skillbox.blog_engine.model.Post;
import ru.skillbox.blog_engine.model.PostComment;
import ru.skillbox.blog_engine.model.User;
import ru.skillbox.blog_engine.services.AuthService;
import ru.skillbox.blog_engine.services.CommentsService;
import ru.skillbox.blog_engine.services.PostService;

@RestController
@RequestMapping("/api/comment")
public class ApiCommentController {
    @Autowired
    private AuthService authService;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentsService commentsService;

    public ApiCommentController(AuthService authService, PostService postService,
                                CommentsService commentsService) {
        this.authService = authService;
        this.postService = postService;
        this.commentsService = commentsService;
    }

    @PostMapping("")
    public ResponseEntity<ResultResponse> addComment(@RequestBody NewCommentRequest comment) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User user = userOptional.get();
        Optional<Post> post = postService.getPostById(comment.getPostId());
        Optional<PostComment> parentComment = Optional.empty();
        if (post.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (comment.getParentId() != null) {
            List<PostComment> postComments = post.get().getPostComments();
            parentComment = commentsService.findCommentById(comment.getParentId());
            if (parentComment.isEmpty() || (!postComments.isEmpty() && !postComments.contains(parentComment.get()))) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        CommentResponse result = new CommentResponse();
        PostComment newComment = commentsService.addComment(user, parentComment.orElse(null),
                                                            post.get(), comment.getText());
        if (newComment != null) {
            result.setResult(true);
            result.setId(newComment.getId());
        } else {
            result.setResult(false);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
