package ru.skillbox.blog_engine.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "post_comments")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private PostComment parentPostComment;

    @ManyToOne(cascade = CascadeType.ALL)
    private Post post;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    private LocalDateTime time;

    @Column(length = 65535, columnDefinition="TEXT")
    @Type(type="text")
    private String text;
}
