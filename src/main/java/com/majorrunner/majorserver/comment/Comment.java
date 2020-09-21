package com.majorrunner.majorserver.comment;

import com.majorrunner.majorserver.post.Post;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Comment {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

}
