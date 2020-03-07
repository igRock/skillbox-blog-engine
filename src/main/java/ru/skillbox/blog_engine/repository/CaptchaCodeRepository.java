package ru.skillbox.blog_engine.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog_engine.model.CaptchaCode;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Repository
@Transactional
public interface CaptchaCodeRepository extends CrudRepository<CaptchaCode, Long> {
    @Modifying
    void deleteByTime(LocalDateTime localDateTime);

    CaptchaCode findBySecretCode(String secretCode);
}
