package com.majorrunner.majorserver.accoutInfo;

import com.majorrunner.majorserver.account.Account;
import com.majorrunner.majorserver.account.AccountDto;
import com.majorrunner.majorserver.account.AccountRepository;
import com.majorrunner.majorserver.category.Category;
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
public class AccountInfoController {

    private final AccountInfoRepository accountInfoRepository;
    private final AccountRepository accountRepository;

    @PostMapping
    public ResponseEntity createCategories(@RequestBody AccountInfoDto.CategoryAddRequest categoryAddRequest) {

        Optional<Account> optionalAccount =
                accountRepository.findByUsername(categoryAddRequest.getAccount().getUsername());

        if (!optionalAccount.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Account account = optionalAccount.get();
        List<Category> categories = categoryAddRequest.getCategories();

        Optional<AccountInfo> optionalAccountInfo = accountInfoRepository.findByAccount(account);

        if (!optionalAccountInfo.isPresent()) {
            AccountInfo accountInfo = AccountInfo.createUserInfo(account, categories);
            account.setAccountInfo(accountInfo);
            accountRepository.save(account);
            return ResponseEntity.ok().build();
        }

        AccountInfo accountInfo = optionalAccountInfo.get();
        accountInfo.setCategories(null);
        accountInfoRepository.delete(accountInfo);

        accountInfo.setCategories(categories);

        account.setAccountInfo(accountInfo);
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
            account.setAccountInfo(AccountInfo.createUserInfo(account, new ArrayList<>()));
        }

        AccountInfo accountInfo = account.getAccountInfo();
        List<Category> categories = accountInfo.getCategories();

        return ResponseEntity.ok().body(categories);
    }
}
