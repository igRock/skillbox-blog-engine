package ru.skillbox.blog_engine.model;

import lombok.Data;
import ru.skillbox.blog_engine.enums.GlobalSettings;

import javax.persistence.*;

@Data
@Entity
@Table(name = "global_settings")
public class GlobalSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private GlobalSettings.Code code;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 5, nullable = false)
    private GlobalSettings.Value value;

}
