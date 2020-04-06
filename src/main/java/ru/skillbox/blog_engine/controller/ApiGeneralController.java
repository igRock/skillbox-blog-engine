package ru.skillbox.blog_engine.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.blog_engine.dto.*;
import ru.skillbox.blog_engine.enums.StatisticsType;
import ru.skillbox.blog_engine.model.User;
import ru.skillbox.blog_engine.services.AuthService;
import ru.skillbox.blog_engine.services.ResponseService;
import ru.skillbox.blog_engine.services.SettingsService;
import ru.skillbox.blog_engine.services.StatisticsService;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {
    @Autowired
    private ResponseService responseService;
    @Autowired
    private AuthService authService;
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private StatisticsService statisticsService;

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

    @PostMapping(value = "/image",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> postImage(@RequestParam("image") MultipartFile image) {
        return responseService.postImage(image);
    }

    @GetMapping("/tag")
    public ResponseEntity<TagsResponse> getTags(@RequestParam(required = false) String query) {
        if (query == null) {
            return null;
        }
        return responseService.getTagsResponse(query);
    }

    @PostMapping("/moderation")
    public ResponseEntity<ResultResponse> postModeration(@RequestBody ModerationRequest moderationRequest) {
        return responseService.moderate(moderationRequest);
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
        return responseService.getCalendarResponse(ldt);
    }
}
