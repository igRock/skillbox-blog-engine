package ru.skillbox.blog_engine.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.blog_engine.dto.ApiInitResponse;
import ru.skillbox.blog_engine.dto.CalendarResponse;
import ru.skillbox.blog_engine.dto.ModerationRequest;
import ru.skillbox.blog_engine.dto.ResultResponse;
import ru.skillbox.blog_engine.dto.SettingsValues;
import ru.skillbox.blog_engine.dto.StatisticsDto;
import ru.skillbox.blog_engine.dto.TagDto;
import ru.skillbox.blog_engine.dto.TagsResponse;
import ru.skillbox.blog_engine.enums.Decision;
import ru.skillbox.blog_engine.enums.ModerationStatus;
import ru.skillbox.blog_engine.enums.StatisticsType;
import ru.skillbox.blog_engine.model.Post;
import ru.skillbox.blog_engine.model.User;
import ru.skillbox.blog_engine.services.AuthService;
import ru.skillbox.blog_engine.services.PostService;
import ru.skillbox.blog_engine.services.SettingsService;
import ru.skillbox.blog_engine.services.StatisticsService;
import ru.skillbox.blog_engine.services.StorageService;
import ru.skillbox.blog_engine.services.TagService;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {
    @Autowired
    private AuthService authService;
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private PostService postService;

    @Autowired
    private TagService tagService;

    public ApiGeneralController(AuthService authService,
                                SettingsService settingsService,
                                StatisticsService statisticsService,
                                PostService postService,
                                TagService tagService) {
        this.authService = authService;
        this.settingsService = settingsService;
        this.statisticsService = statisticsService;
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping("/init")
    public ResponseEntity<ApiInitResponse> getApiInit() {
        ApiInitResponse response = new ApiInitResponse();
        response.setCopyright("Jack Jones");
        response.setCopyrightFrom("2020");
        response.setEmail("jackjones@mail.ru");
        response.setPhone("+79999999999");
        response.setSubtitle("SubTitle");
        response.setTitle("Title");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/tag")
    public ResponseEntity<TagsResponse> getTags(@RequestParam(required = false) String query) {
        if (query == null) {
            return null;
        }
        List<TagDto> tagDtoList = tagService.getTagDtoListByQuery(query);
        TagsResponse tagsResponse = new TagsResponse();
        tagsResponse.setTags(tagDtoList);
        return new ResponseEntity<>(tagsResponse, HttpStatus.OK);
    }

    @PostMapping("/moderation")
    public ResponseEntity<ResultResponse> postModeration(@RequestBody ModerationRequest moderationRequest) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User user = userOptional.get();
        if (!user.getIsModerator()) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        final Optional<Post> postOptional = postService.getPostById(moderationRequest.getPostId());
        if (postOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        final Post post = postOptional.get();

        final User postModerator = post.getModeratedBy();
        if (postModerator != null && !postModerator.equals(user)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        Decision decision = Decision.valueOf(moderationRequest.getDecision().toUpperCase());
        ModerationStatus status = (decision.equals(Decision.ACCEPT)) ?
                                  ModerationStatus.ACCEPTED : ModerationStatus.DECLINED;
        post.setModerationStatus(status);
        post.setModeratedBy(user);
        Post updatedPost = postService.savePost(post);

        ResultResponse result = new ResultResponse();
        result.setResult(updatedPost != null);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/settings")
    public ResponseEntity<SettingsValues> updateSettings(@RequestBody SettingsValues settings) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User user = userOptional.get();
        if (!user.getIsModerator()) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        SettingsValues updatedSettings = settingsService.saveSettings(settings);
        return new ResponseEntity<>(updatedSettings, HttpStatus.OK);
    }

    @GetMapping("/settings")
    public ResponseEntity<SettingsValues> getSettings() {
        return new ResponseEntity<>(settingsService.getSettings(), HttpStatus.OK);
    }

    @GetMapping("/statistics/{statisticsType}")
    public ResponseEntity<StatisticsDto> getStatistics(@PathVariable StatisticsType statisticsType) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        boolean isStatsPublic = settingsService.isStatsPublic();
        if (userOptional.isPresent()) {
            if (StatisticsType.ALL.equals(statisticsType) && isStatsPublic) {
                return new ResponseEntity<>(statisticsService.getStatistics(null), HttpStatus.OK);
            }
            return new ResponseEntity<>(statisticsService.getStatistics(userOptional.get()), HttpStatus.OK);
        }
        if (isStatsPublic) {
            return new ResponseEntity<>(statisticsService.getStatistics(null), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/calendar")
    public ResponseEntity<CalendarResponse> getCalendar(
        @RequestParam(required = false) String year) {
        Integer currentYear = LocalDate.now().getYear();
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(currentYear,1, 1),
                                             LocalTime.MIDNIGHT);
        if (year != null) {
            ldt = ldt.withYear(Integer.parseInt(year));
        }
        List<Post> allPostList = postService.getAllPostsFromRepository(true, ModerationStatus.ACCEPTED);
        List<Post> postList = postService.searchByDate(allPostList, ldt, LocalDateTime.now());
        Map<String, Long> postsCountPerYear = postList.stream()
            .collect(Collectors.groupingBy(p -> p.getTime().toString().split("T")[0],
                                           Collectors.counting()));
        List<Integer> postYears = postList.stream()
            .map(p -> p.getTime().getYear())
            .collect(Collectors.toList());
        CalendarResponse calendarResponse = new CalendarResponse();
        calendarResponse.setYears(postYears);
        calendarResponse.setPosts(postsCountPerYear);
        return new ResponseEntity<>(calendarResponse, HttpStatus.OK);
    }
}
