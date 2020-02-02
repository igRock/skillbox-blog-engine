package ru.skillbox.blog_engine.repository;

import org.springframework.data.repository.CrudRepository;
import ru.skillbox.blog_engine.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
}
