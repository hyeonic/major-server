package com.majorrunner.majorserver.comment;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CommentDto {

    @Data
    public static class CreateCommentRequest {
        @NotEmpty
        private String comment;

        public CreateCommentRequest(String comment) {
            this.comment = comment;
        }
    }

    @Data
    public static class CreateCommentResponse {
        private String comment;

        public CreateCommentResponse(String comment) {
            this.comment = comment;
        }
    }

}
