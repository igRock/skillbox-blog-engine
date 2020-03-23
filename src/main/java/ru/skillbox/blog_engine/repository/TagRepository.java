package ru.skillbox.blog_engine.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog_engine.model.Tag;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {
}
