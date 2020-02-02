package ru.skillbox.blog_engine;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.skillbox.blog_engine.model.GlobalSetting;
import ru.skillbox.blog_engine.services.GlobalSettingsService;
import ru.skillbox.blog_engine.utils.HibernateSessionFactoryUtil;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
