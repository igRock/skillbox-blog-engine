package ru.skillbox.blog_engine.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_comments")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PostComment getParentPostComment() {
        return parentPostComment;
    }

    public void setParentPostComment(PostComment parentPostComment) {
        this.parentPostComment = parentPostComment;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
