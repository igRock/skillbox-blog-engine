package ru.skillbox.blog_engine.model;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Type;
import ru.skillbox.blog_engine.enums.ModerationStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@ToString(callSuper = true, of = {"title"})
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "is_active")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('NEW', 'ACCEPTED', 'DECLINED')", name = "moderation_status")
    private ModerationStatus moderationStatus;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName="id")
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "moderator_id", referencedColumnName="id")
    private User moderatedBy;

    private LocalDateTime time;

    private String title;

    private String text;

    @Column(name = "view_count")
    private int viewCount;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "tag2post",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private List<Tag> tags;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PostVote> postVotes;

    @OneToMany(mappedBy = "post")
    private List<PostComment> postComments;

    public void incrementViewCount() {
        this.viewCount++;
    }
}
