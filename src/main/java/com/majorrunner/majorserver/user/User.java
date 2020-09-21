package com.majorrunner.majorserver.user;

import com.majorrunner.majorserver.Likes.Likes;
import com.majorrunner.majorserver.post.Post;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String password;
    private String nickName;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Likes> likes;

}