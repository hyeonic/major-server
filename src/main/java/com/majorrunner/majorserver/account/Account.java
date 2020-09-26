package com.majorrunner.majorserver.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.majorrunner.majorserver.Like.Like;
import com.majorrunner.majorserver.post.Post;
import com.majorrunner.majorserver.accoutInfo.AccountInfo;
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
public class Account {

    @Id @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    private String email;
    private String password;
    private String nickName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;

    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AccountInfo accountInfo;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Like> likes = new ArrayList<>();

    // == 연관관계 메서드 == //
    public void addPost(Post post) {
        posts.add(post);
        post.setAccount(this);
    }

    public void addLike(Like like) {
        likes.add(like);
        like.setAccount(this);
    }

    // == 생성 메서드 == //
    // 정적 팩토리 메서드
    public static Account createAccount(String email, String password, String nickName, Set<AccountRole> roles) {
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(password);
        account.setNickName(nickName);
        account.setRoles(roles);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        return account;
    }

    // == 연관관계 메서드 == //
    public void setUserInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
        accountInfo.setAccount(this);
    }
}