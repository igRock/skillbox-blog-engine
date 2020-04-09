package ru.skillbox.blog_engine.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog_engine.model.Post;
import ru.skillbox.blog_engine.model.PostVote;
import ru.skillbox.blog_engine.model.User;

import java.util.List;

@Repository
public interface VotesRepository extends CrudRepository<PostVote, Integer> {
    List<PostVote> findByPostAndValue(Post post, byte value);

    PostVote findByUserAndPost(User user, Post post);

    @Query("SELECT COUNT(*) FROM PostVote v WHERE (:user IS NULL OR v.user = :user) AND v.value = :voteValue")
    Integer countByUserAndValue(@Param("user") User user, @Param("voteValue") Integer voteValue);
}
