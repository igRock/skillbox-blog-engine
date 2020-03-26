package ru.skillbox.blog_engine.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "captcha_codes")
public class CaptchaCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime time;
    private String code;

    @Column(name = "secret_code")
    private String secretCode;
}
