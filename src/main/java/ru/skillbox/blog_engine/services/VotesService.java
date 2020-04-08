package ru.skillbox.blog_engine.services;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.blog_engine.enums.Vote;
import ru.skillbox.blog_engine.model.Post;
import ru.skillbox.blog_engine.model.PostVote;
import ru.skillbox.blog_engine.model.User;
import ru.skillbox.blog_engine.repository.VotesRepository;

@Service
public class VotesService {
    @Autowired
    private VotesRepository votesRepository;

    public VotesService(VotesRepository votesRepository) {
        this.votesRepository = votesRepository;
    }

    public Boolean vote(Vote voteType, User user, Post post) {
        Integer voteRequested = voteType.equals(Vote.LIKE) ? 1 : -1;
        PostVote postVote = votesRepository.findByUserAndPost(user, post);
        if (postVote == null) {
            PostVote newVote = new PostVote();
            newVote.setPost(post);
            newVote.setUser(user);
            newVote.setValue(voteRequested);
            newVote.setTime(LocalDateTime.now());
            votesRepository.save(newVote);
            return true;
        }
        if (voteRequested == postVote.getValue()) {
            return false;
        }
        votesRepository.delete(postVote);
        PostVote newVote = new PostVote();
        newVote.setPost(post);
        newVote.setUser(user);
        newVote.setValue(voteRequested);
        newVote.setTime(LocalDateTime.now());
        votesRepository.save(newVote);
        return true;
    }

    public Integer countByUserAndValue(User user, Vote voteValue) {
        Integer voteIntValue = Vote.LIKE.equals(voteValue) ? 1 : -1;
        return votesRepository.countByUserAndValue(user, voteIntValue);
    }
}
