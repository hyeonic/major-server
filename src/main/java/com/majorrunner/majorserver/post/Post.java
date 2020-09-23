package com.majorrunner.majorserver.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.majorrunner.majorserver.Like.Like;
import com.majorrunner.majorserver.category.Category;
import com.majorrunner.majorserver.comment.Comment;
import com.majorrunner.majorserver.comment.CommentStatus;
import com.majorrunner.majorserver.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @Embedded
    private Article article;

    @Enumerated(EnumType.STRING)
    private CommentStatus status; // SHOW, HIDE

    private int views;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Like> likes = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // == 연관관계 메서드 == //
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void addLikes(Like like) {
        likes.add(like);
        like.setPost(this);
    }

    public void setCategory(Category category) {
        category = category;
        category.getPosts().add(this);
    }

    // == 생성 메서드 == //
    // 정적 팩토리 메서드
    public static Post createPost(Article article, CommentStatus commentStatus,
                                  User user, Category category) {
        Post post = new Post();
        post.setArticle(article);
        post.setStatus(commentStatus);
        post.setUser(user);
        post.setCategory(category);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        return post;
    }

    // == 비즈니스 로직 == //
    public void changeStatus() {
        if (status.equals(CommentStatus.HIDE)) {
            status = CommentStatus.SHOW;
        }else if (status.equals(CommentStatus.SHOW)) {
            status = CommentStatus.HIDE;
        }
    }
}