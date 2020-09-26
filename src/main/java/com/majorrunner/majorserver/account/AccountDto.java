package com.majorrunner.majorserver.account;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AccountDto {

    @Data
    public static class CreateAccountRequest {
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
        @NotEmpty
        private String nickName;

        public CreateAccountRequest(String username, String password, String nickName) {
            this.username = username;
            this.password = password;
            this.nickName = nickName;
        }
    }

    @Data
    public static class CreateAccountResponse {
        private String username;
        private String nickName;

        public CreateAccountResponse(String username, String nickName) {
            this.username = username;
            this.nickName = nickName;
        }
    }

    @Data
    public static class ReadAccountRequest {
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;

        public ReadAccountRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    @Data
    public static class ReadAccountResponse {
        private String username;

        public ReadAccountResponse(String username) {
            this.username = username;
        }
    }
}
