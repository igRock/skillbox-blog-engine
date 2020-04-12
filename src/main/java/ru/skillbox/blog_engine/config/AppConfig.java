package ru.skillbox.blog_engine.config;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AppConfig {
    private final Map<String, Integer> sessions = new HashMap<>();
    private final Captcha captcha = new Captcha();


    public void addSession(String sessionId, Integer userId) {
        sessions.put(sessionId, userId);
    }

    public Map<String, Integer> getSessions() {
        return sessions;
    }

    public Integer getUserIdBySessionId(String sessionId) {
        return sessions.getOrDefault(sessionId, null);
    }

    public Integer deleteSessionById(String sessionId) {
        return sessions.remove(sessionId);
    }

    public Captcha getCaptcha() {
        return captcha;
    }

    @Data
    public static class Captcha {
        private Integer codeLength = 4;
        private Integer hoursToBeUpdated = 1;
    }
}
