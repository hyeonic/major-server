package com.majorrunner.majorserver.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.majorrunner.majorserver.account.Account;
import com.majorrunner.majorserver.post.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 생성자를 protected로 두어 생성을 막아둠
public class Comment {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // == 생성 메서드 == //
    // 정적 팩토리 메서드
    public static Comment createComment(String com, Post post) {
        Comment comment = new Comment();
        comment.setComment(com);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        return comment;
    }
}
