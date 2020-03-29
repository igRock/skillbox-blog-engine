package ru.skillbox.blog_engine.services;

import com.github.cage.Cage;
import com.github.cage.GCage;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;
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

    private void deleteOutdatedCaptchas(Integer hoursToBeUpdated) {
        final LocalDateTime timeToBeUpdates = LocalDateTime.now().minusHours(hoursToBeUpdated);
        captchaCodeRepository.deleteByTime(timeToBeUpdates);
    }

    public boolean isValidCaptcha(String userCaptcha, String userCaptchaSecretCode) {
        CaptchaCode dbCaptcha = captchaCodeRepository.findBySecretCode(userCaptchaSecretCode);
        return dbCaptcha != null && userCaptcha.equals(dbCaptcha.getCode());
    }

    private String getRandomCode(final Integer length) {
        final int LEFT = 48;             // '0'
        final int RIGHT = 122;           // 'z'

        return new Random().ints(LEFT, RIGHT + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String generateBase64Image(String text) {
//        String result = "";
//        try {
//            result = new String(Base64.getEncoder().encode(CAGE.draw(text)), "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return result;
        String result = "";
        try {
            BufferedImage captchaImage = ImageIO.read(new ByteArrayInputStream(CAGE.draw(text)));
            captchaImage = resizeImage(captchaImage);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(captchaImage, "jpg", baos);
            baos.flush();
            result = new String(Base64.getEncoder().encode(baos.toByteArray()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static BufferedImage resizeImage(BufferedImage captchaImage) {
        int newWidth = 100;
        int newHeight = (int) Math.round(captchaImage.getHeight() / (captchaImage.getWidth() / (double) newWidth));
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        int widthStep = captchaImage.getWidth() / newWidth;
        int hightStep = captchaImage.getHeight() / newHeight;
        for (int x = 0; x < newWidth; x++) {
            for (int y = 0; y < newHeight; y++) {
                int rgb = captchaImage.getRGB(x * widthStep, y * hightStep);
                newImage.setRGB(x, y, rgb);
            }
        }
        return newImage;
    }
}
