package ru.skillbox.blog_engine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.blog_engine.dto.SettingsValues;
import ru.skillbox.blog_engine.enums.GlobalSettings;
import ru.skillbox.blog_engine.enums.GlobalSettings.Code;
import ru.skillbox.blog_engine.enums.GlobalSettings.Value;
import ru.skillbox.blog_engine.model.GlobalSetting;
import ru.skillbox.blog_engine.repository.GlobalSettingsRepository;

@Service
public class SettingsService {
    @Autowired
    private GlobalSettingsRepository settingsRepository;

    public SettingsService(GlobalSettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public SettingsValues saveSettings(SettingsValues settings) {
        saveSetting(GlobalSettings.Code.MULTIUSER_MODE, settings.getMultiuserMode());
        saveSetting(GlobalSettings.Code.POST_PREMODERATION, settings.getPostPremoderation());
        saveSetting(GlobalSettings.Code.STATISTICS_IS_PUBLIC, settings.getStatisticsIsPublic());
        return getSettings();
    }

    private GlobalSetting saveSetting(GlobalSettings.Code code, boolean valueToUpdate) {
        GlobalSettings.Value value = valueToUpdate ? GlobalSettings.Value.YES : GlobalSettings.Value.NO;
        GlobalSetting globalSetting = settingsRepository.findByCode(code).orElse(generateDefaultGS());
        if (!value.equals(globalSetting.getValue())) {
            globalSetting.setValue(value);
            settingsRepository.save(globalSetting);
        }
        return globalSetting;
    }

    public SettingsValues getSettings() {
        SettingsValues settings = new SettingsValues();
        settingsRepository.findAll().forEach(setting -> {
            GlobalSetting MULTIUSER_MODE = settingsRepository.findByCode(GlobalSettings.Code.MULTIUSER_MODE)
                .orElse(generateDefaultGS());
            GlobalSetting POST_PREMODERATION = settingsRepository.findByCode(GlobalSettings.Code.POST_PREMODERATION)
                .orElse(generateDefaultGS());
            GlobalSetting STATISTICS_IS_PUBLIC = settingsRepository.findByCode(GlobalSettings.Code.STATISTICS_IS_PUBLIC)
                .orElse(generateDefaultGS());
            settings.setMultiuserMode(MULTIUSER_MODE.getValue().getValue());
            settings.setPostPremoderation(POST_PREMODERATION.getValue().getValue());
            settings.setStatisticsIsPublic(STATISTICS_IS_PUBLIC.getValue().getValue());
        });
        return settings;
    }

    public boolean isStatsPublic() {
        return settingsRepository.findByCode(GlobalSettings.Code.STATISTICS_IS_PUBLIC)
            .orElse(generateDefaultGS()).getValue().getValue();
    }

    private static GlobalSetting generateDefaultGS() {
        GlobalSetting gs = new GlobalSetting();
        gs.setValue(Value.YES);
        gs.setCode(Code.STATISTICS_IS_PUBLIC);
        gs.setName(Code.STATISTICS_IS_PUBLIC.getName());
        return gs;
    }

}
