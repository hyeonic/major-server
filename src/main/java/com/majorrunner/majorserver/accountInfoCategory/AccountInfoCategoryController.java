package com.majorrunner.majorserver.accountInfoCategory;

import com.majorrunner.majorserver.account.Account;
import com.majorrunner.majorserver.account.AccountRepository;
import com.majorrunner.majorserver.accoutInfo.AccountInfo;
import com.majorrunner.majorserver.accoutInfo.AccountInfoRepository;
import com.majorrunner.majorserver.category.Category;
import com.majorrunner.majorserver.category.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/account-info", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class AccountInfoCategoryController {

    private final AccountRepository accountRepository;
    private final AccountInfoRepository accountInfoRepository;

    @PostMapping
    public ResponseEntity createAccountInfo(@RequestBody AccountInfoCategoryDto.request accountInfoCategoryRequest) {

        Optional<Account> optionalAccount =
                accountRepository.findByUsername(accountInfoCategoryRequest.getAccount().getUsername());

        if (!optionalAccount.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Account account = optionalAccount.get();
        account.setAccountInfo(null);
        accountInfoRepository.deleteByAccount(account);

        List<Category> categories = accountInfoCategoryRequest.getCategories();
        AccountInfo accountInfo = AccountInfo.createUserInfo(account);

        account.setAccountInfo(accountInfo);

        for (Category category : categories) {
            AccountInfoCategory accountInfoCategory = new AccountInfoCategory(account.getAccountInfo(), category);
            account.getAccountInfo().addCategory(accountInfoCategory);
        }
        accountRepository.save(account);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/nick-name/{nickName}")
    public ResponseEntity getAccountInfo(@PathVariable String nickName) {

        Optional<Account> optionalAccount = accountRepository.findByNickName(nickName);

        if (!optionalAccount.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Account account = optionalAccount.get();
        if (account.getAccountInfo() == null) {
            AccountInfo accountInfo = AccountInfo.createUserInfo(account);
            List<AccountInfoCategory> accountInfoCategories = accountInfo.getAccountInfoCategories();
            return ResponseEntity.ok().body(accountInfoCategories);
        }

        AccountInfo accountInfo = account.getAccountInfo();
        List<AccountInfoCategory> categories = accountInfo.getAccountInfoCategories();
        List<CategoryDto.ReadCategoryResponse> readCategoryResponses = new ArrayList<>();

        for (AccountInfoCategory accountInfoCategory : categories) {
            readCategoryResponses.add(new CategoryDto.ReadCategoryResponse(
                    accountInfoCategory.getCategory().getId(),
                    accountInfoCategory.getCategory().getCategoryName(),
                    accountInfoCategory.getCategory().getSubCategoryName()));
        }

        return ResponseEntity.ok().body(readCategoryResponses);
    }
}
