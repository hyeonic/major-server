package com.majorrunner.majorserver.accountInfoCategory;

import com.majorrunner.majorserver.account.Account;
import com.majorrunner.majorserver.category.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class AccountInfoCategoryDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class request {
        @NotNull
        private Account account;
        @NotEmpty
        private List<Category> categories;
    }
}
