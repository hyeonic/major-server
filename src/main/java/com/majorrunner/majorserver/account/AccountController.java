package com.majorrunner.majorserver.account;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/account", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class AccountController {

    private final ModelMapper modelMapper;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @PostMapping("/signup")
    public ResponseEntity createAccount(@RequestBody AccountDto.CreateAccountRequest createAccountRequest) {

        Optional<Account> optionalAccount = accountRepository.findByUsername(createAccountRequest.getUsername());

        if (optionalAccount.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Account> optionalAccount2 = accountRepository.findByNickName(createAccountRequest.getNickName());

        if (optionalAccount2.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        Account account = modelMapper.map(createAccountRequest, Account.class);
        Account savedAccount = accountService.saveAccount(account);

        AccountDto.CreateAccountResponse createAccountResponse =
                new AccountDto.CreateAccountResponse(savedAccount.getUsername(),
                        savedAccount.getNickName(), savedAccount.getCreatedAt(), savedAccount.getUpdatedAt());

        return ResponseEntity.ok().body(createAccountResponse);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity getAccount(@PathVariable String username) {
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);

        if (!optionalAccount.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Account account = optionalAccount.get();
        AccountDto.CreateAccountResponse createAccountResponse =
                new AccountDto.CreateAccountResponse(account.getUsername(), account.getNickName(),
                        account.getCreatedAt(), account.getUpdatedAt());


        return ResponseEntity.ok(createAccountResponse);
    }
}