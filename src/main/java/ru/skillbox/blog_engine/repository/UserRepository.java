package ru.skillbox.blog_engine.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog_engine.model.User;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findByIsModeratorTrue();
    List<User> findByIsModeratorFalse();
    User findByEmail(String email);
    User findByCode(String code);
}
