package ru.skillbox.blog_engine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.blog_engine.model.Post;
import ru.skillbox.blog_engine.model.PostComment;
import ru.skillbox.blog_engine.model.User;
import ru.skillbox.blog_engine.repository.CommentsRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentsService {
    @Autowired
    private CommentsRepository commentsRepository;

    public CommentsService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    public Optional<PostComment> findCommentById(Integer id) {
        return commentsRepository.findById(id);
    }

    public PostComment addComment(User user, PostComment parentComment, Post post, String text) {
        PostComment newComment = new PostComment();
        newComment.setParentPostComment(parentComment);
        newComment.setUser(user);
        newComment.setPost(post);
        newComment.setText(text);
        newComment.setTime(LocalDateTime.now());
        // Сразу return
        PostComment savedComment = commentsRepository.save(newComment);
        return savedComment;
    }


}
