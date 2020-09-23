package com.majorrunner.majorserver.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.majorrunner.majorserver.Like.Like;
import com.majorrunner.majorserver.post.Post;
import com.majorrunner.majorserver.userInfo.UserInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter  @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String password;
    private String nickName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserInfo userinfo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Like> likes = new ArrayList<>();

    // == 연관관계 메서드 == //
    public void addPost(Post post) {
        posts.add(post);
        post.setUser(this);
    }

    public void addLike(Like like) {
        likes.add(like);
        like.setUser(this);
    }

    // == 생성 메서드 == //
    // 정적 팩토리 메서드
    public static User createUser(String email, String password, String nickName) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setNickName(nickName);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return user;
    }

    // == 연관관계 메서드 == //
    public void setUserInfo(UserInfo userInfo) {
        this.userinfo = userInfo;
        userInfo.setUser(this);
    }
}