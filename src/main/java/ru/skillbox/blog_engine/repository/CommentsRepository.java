package ru.skillbox.blog_engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog_engine.model.Post;
import ru.skillbox.blog_engine.model.PostComment;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<PostComment, Integer> {
    // Переписать за JPQL
    List<PostComment> findByPost(Post post);
}
