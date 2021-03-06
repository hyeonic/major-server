package com.majorrunner.majorserver.accoutInfo;

import com.majorrunner.majorserver.account.Account;
import com.majorrunner.majorserver.category.Category;
import lombok.*;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class AccountInfoDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryAddRequest {

        @NotNull
        private Account account;
        @NotEmpty
        private List<Category> categories;
    }
}
