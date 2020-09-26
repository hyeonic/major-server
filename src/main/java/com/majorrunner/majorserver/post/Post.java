package com.majorrunner.majorserver.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.majorrunner.majorserver.Like.Like;
import com.majorrunner.majorserver.account.Account;
import com.majorrunner.majorserver.category.Category;
import com.majorrunner.majorserver.comment.Comment;
import com.majorrunner.majorserver.comment.CommentStatus;
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

    private String title;
    private String contents;

    @Enumerated(EnumType.STRING)
    private CommentStatus status; // SHOW, HIDE

    private int views;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

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
        this.category = category;
        category.getPosts().add(this);
    }

    // == 생성 메서드 == //
    // 정적 팩토리 메서드
    public static Post createPost(String title, String contents, CommentStatus commentStatus,
                                  Account account, Category category) {
        Post post = new Post();
        post.setTitle(title);
        post.setContents(contents);
        post.setStatus(commentStatus);
        post.setAccount(account);
        post.setViews(0);
        post.setCategory(category);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        return post;
    }

    // == 비즈니스 로직 == //
    public Post updatePost(PostDto postDto, Post post) {
        post.setTitle(postDto.getTitle());
        post.setContents(postDto.getContents());
        post.setStatus(postDto.getCommentStatus());
        post.setCategory(postDto.getCategory());
        post.setComments(postDto.getComments());
        post.setLikes(postDto.getLikes());

        post.setUpdatedAt(LocalDateTime.now());

        return post;
    }

    public void changeStatus() {
        if (status.equals(CommentStatus.HIDE)) {
            status = CommentStatus.SHOW;
        }else if (status.equals(CommentStatus.SHOW)) {
            status = CommentStatus.HIDE;
        }
    }

    public void incrementViews() {
        views += views;
    }
}