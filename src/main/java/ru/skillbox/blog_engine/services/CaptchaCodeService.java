package ru.skillbox.blog_engine.services;

import com.github.cage.Cage;
import com.github.cage.GCage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.blog_engine.config.AppConfig;
import ru.skillbox.blog_engine.model.CaptchaCode;
import ru.skillbox.blog_engine.repository.CaptchaCodeRepository;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

@Service
public class CaptchaCodeService {
    @Autowired
    private AppConfig appConfig;

    @Autowired
    private CaptchaCodeRepository captchaCodeRepository;

    private static final Cage CAGE = new GCage();

    public CaptchaCode getCaptcha() {
        final int HOURS_TO_BE_UPDATED = appConfig.getCaptcha().getHoursToBeUpdated();
        final int CODE_LENGTH = appConfig.getCaptcha().getCodeLength();

        // Outdated captchas cleanup
        deleteOutdatedCaptchas(HOURS_TO_BE_UPDATED);

        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setTime(LocalDateTime.now());
        captchaCode.setCode(getRandomCode(CODE_LENGTH));
        captchaCode.setSecretCode(UUID.randomUUID().toString());

        return captchaCodeRepository.save(captchaCode);
    }

    private void deleteOutdatedCaptchas(int hoursToBeUpdated) {
        final LocalDateTime timeToBeUpdates = LocalDateTime.now().minusHours(hoursToBeUpdated);
        captchaCodeRepository.deleteByTime(timeToBeUpdates);
    }

    public boolean isValidCaptcha(String userCaptcha, String userCaptchaSecretCode) {
        CaptchaCode dbCaptcha = captchaCodeRepository.findBySecretCode(userCaptchaSecretCode);
        return dbCaptcha != null && userCaptcha.equals(dbCaptcha.getCode());
    }

    private String getRandomCode(final int length) {
        final int LEFT = 48;             // '0'
        final int RIGHT = 122;           // 'z'

        return new Random().ints(LEFT, RIGHT + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String generateBase64Image(String text) {
        String result = "";
        try {
            result = new String(Base64.getEncoder().encode(CAGE.draw(text)), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}