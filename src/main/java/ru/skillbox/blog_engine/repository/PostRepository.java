package ru.skillbox.blog_engine.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog_engine.model.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
}
