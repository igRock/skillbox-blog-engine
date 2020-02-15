package ru.skillbox.blog_engine.repository;

import org.springframework.data.repository.CrudRepository;
import ru.skillbox.blog_engine.model.Tag;

public interface TagRepository extends CrudRepository<Tag, Integer> {
}
