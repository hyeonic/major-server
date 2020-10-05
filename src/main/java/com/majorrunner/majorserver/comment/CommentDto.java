package com.majorrunner.majorserver.comment;

import com.majorrunner.majorserver.account.Account;
import com.majorrunner.majorserver.account.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CommentDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCommentRequest {
        @NotEmpty
        private String comment;
        @NotNull
        private Account account;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCommentResponse {
        private String comment;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryCommentResponse {
        private Long id;
        private String comment;
        private AccountDto.CreateAccountResponse account;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public QueryCommentResponse(Comment comment) {
            this.id = comment.getId();
            this.comment = comment.getComment();

            Account account = comment.getAccount();
            this.account = new AccountDto.CreateAccountResponse(account.getUsername(), account.getNickName());

            this.createdAt = comment.getCreatedAt();
            this.updatedAt = comment.getUpdatedAt();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCommentRequest {
        @NotEmpty
        private String comment;
    }

}
