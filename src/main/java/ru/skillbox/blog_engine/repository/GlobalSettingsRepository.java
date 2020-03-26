package ru.skillbox.blog_engine.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog_engine.enums.GlobalSettings;
import ru.skillbox.blog_engine.model.GlobalSetting;

@Repository
public interface GlobalSettingsRepository extends CrudRepository<GlobalSetting, Integer> {
    GlobalSetting findByCode(GlobalSettings.Code code);
}
