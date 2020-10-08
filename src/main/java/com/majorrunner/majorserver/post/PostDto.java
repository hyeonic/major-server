package com.majorrunner.majorserver.post;

import com.majorrunner.majorserver.Like.Like;
import com.majorrunner.majorserver.Like.LikeDto;
import com.majorrunner.majorserver.account.Account;
import com.majorrunner.majorserver.account.AccountDto;
import com.majorrunner.majorserver.category.Category;
import com.majorrunner.majorserver.category.CategoryDto;
import com.majorrunner.majorserver.comment.Comment;
import com.majorrunner.majorserver.comment.CommentDto;
import com.majorrunner.majorserver.comment.CommentStatus;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PostDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreatePostRequest {
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
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadPostResponse {
        private String title;
        private String contents;
        private CommentStatus commentStatus;
        private AccountDto.CreateAccountResponse account;
        private CategoryDto.ReadCategoryResponse category;
        private List<CommentDto.CreateCommentResponse> comments = new ArrayList<>();
        private List<LikeDto.ReadLikeResponse> likes = new ArrayList<>();
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public ReadPostResponse(Post post) {
            this.title = post.getTitle();
            this.contents = post.getContents();
            this.commentStatus = post.getStatus();

            Account account = post.getAccount();
            this.account = new AccountDto.CreateAccountResponse(account.getUsername(), account.getNickName(),
                    account.getCreatedAt(), account.getUpdatedAt());
            Category category = post.getCategory();
            this.category = new CategoryDto.ReadCategoryResponse(category.getId(), category.getCategoryName(),
                    category.getSubCategoryName());

            List<Comment> comments = post.getComments();
            for (Comment comment : comments) {
                CommentDto.CreateCommentResponse commentResponse = new CommentDto.CreateCommentResponse(comment.getComment());
                this.comments.add(commentResponse);
            }

            List<Like> likes = post.getLikes();
            for (Like like : likes) {
                LikeDto.ReadLikeResponse readLikeResponse = new LikeDto.ReadLikeResponse(like.getAccount().getUsername(), like.getCreateAt());
                this.likes.add(readLikeResponse);
            }

            this.createdAt = post.getCreatedAt();
            this.updatedAt = post.getUpdatedAt();
        }
    }

}
