package com.majorrunner.majorserver.post;

import com.majorrunner.majorserver.Likes.Likes;
import com.majorrunner.majorserver.category.Category;
import com.majorrunner.majorserver.comment.Comment;
import com.majorrunner.majorserver.comment.CommentStatus;
import com.majorrunner.majorserver.user.User;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String title;
    private String contents;
    private String filePath; // 좀 더 찾아 볼 것
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;

    private int views;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy = "post")
    private List<Comment> commnets; //

    @OneToMany(mappedBy = "post")
    private List<Likes> likes; //

}