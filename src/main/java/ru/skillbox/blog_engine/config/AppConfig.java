package ru.skillbox.blog_engine.config;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AppConfig {
    private final Map<String, Integer> sessions = new HashMap<>();
    private final Captcha captcha = new Captcha();


    public void addSession(String sessionId, int userId) {
        sessions.put(sessionId, userId);
    }

    public Map<String, Integer> getSessions() {
        return sessions;
    }

    public int getUserIdBySessionId(String sessionId) {
        return sessions.getOrDefault(sessionId, null);
    }

    public int deleteSessionById(String sessionId) {
        return sessions.remove(sessionId);
    }

    public Captcha getCaptcha() {
        return captcha;
    }

    public static class Captcha {
        private int codeLength = 5;
        private int hoursToBeUpdated = 1;

        public int getCodeLength() {
            return codeLength;
        }

        public void setCodeLength(int codeLength) {
            this.codeLength = codeLength;
        }

        public int getHoursToBeUpdated() {
            return hoursToBeUpdated;
        }

        public void setHoursToBeUpdated(int hoursToBeUpdated) {
            this.hoursToBeUpdated = hoursToBeUpdated;
        }
    }
}
