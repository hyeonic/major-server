package com.majorrunner.majorserver.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
public class AccountDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAccountRequest {
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
        @NotEmpty
        private String nickName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAccountResponse {
        private String username;
        private String nickName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadAccountRequest {
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadAccountResponse {
        private String username;
    }
}
