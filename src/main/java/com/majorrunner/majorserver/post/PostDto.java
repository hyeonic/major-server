package com.majorrunner.majorserver.post;

import com.majorrunner.majorserver.Like.Like;
import com.majorrunner.majorserver.account.Account;
import com.majorrunner.majorserver.category.Category;
import com.majorrunner.majorserver.comment.Comment;
import com.majorrunner.majorserver.comment.CommentStatus;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    @NotEmpty
    private String title;
    @NotEmpty
    private String contents;
    @NotNull
    private CommentStatus commentStatus;
    @NotNull
    private Account account;
    @NotNull
    private Category category;

    private List<Comment> comments;
    private List<Like> likes;

}
