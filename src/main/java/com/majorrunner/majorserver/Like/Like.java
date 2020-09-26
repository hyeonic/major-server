package com.majorrunner.majorserver.Like;

import com.majorrunner.majorserver.account.Account;
import com.majorrunner.majorserver.post.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 생성자를 protected로 두어 생성을 막아둠
public class Like {

    @Id @GeneratedValue
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private LocalDateTime createAt;

    // == 생성 메서드 == //
    public static Like createLike(Post post, Account account) {
        Like like = new Like();
        like.setPost(post);
        like.setAccount(account);
        like.setCreateAt(LocalDateTime.now());

        return like;
    }
}
