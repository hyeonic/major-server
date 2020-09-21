package com.majorrunner.majorserver.Likes;

import com.majorrunner.majorserver.post.Post;
import com.majorrunner.majorserver.user.User;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Likes {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private LocalDateTime createAt;

}
