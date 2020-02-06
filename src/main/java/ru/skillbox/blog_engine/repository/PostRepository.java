package ru.skillbox.blog_engine.repository;

import org.springframework.data.repository.CrudRepository;
import ru.skillbox.blog_engine.model.Post;

public interface PostRepository extends CrudRepository<Post, Integer> {
}
