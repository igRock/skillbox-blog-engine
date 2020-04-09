package ru.skillbox.blog_engine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.blog_engine.dto.StatisticsDto;
import ru.skillbox.blog_engine.enums.Vote;
import ru.skillbox.blog_engine.model.User;

@Service
public class StatisticsService {
    @Autowired
    private AuthService authService;
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private PostService postService;
    @Autowired
    private VotesService votesService;

    public StatisticsService(AuthService authService, SettingsService settingsService,
                             PostService postService, VotesService votesService) {
        this.authService = authService;
        this.settingsService = settingsService;
        this.postService = postService;
        this.votesService = votesService;
    }

    public StatisticsDto getStatistics(User user) {
        StatisticsDto statisticsDto = new StatisticsDto();

        statisticsDto.setPostsCount(postService.countByUser(user));
        statisticsDto.setLikesCount(votesService.countByUserAndValue(user, Vote.LIKE));
        statisticsDto.setDislikesCount(votesService.countByUserAndValue(user, Vote.DISLIKE));
        statisticsDto.setViewsCount(postService.countViewsByUser(user));
        statisticsDto.setFirstPublication(postService.getFirstPostDate(user));

        return statisticsDto;
    }
}
