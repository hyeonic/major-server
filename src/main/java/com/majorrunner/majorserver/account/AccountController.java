package com.majorrunner.majorserver.account;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        Account account = modelMapper.map(createAccountRequest, Account.class);
        Account savedAccount = accountService.saveAccount(account);

        AccountDto.CreateAccountResponse createAccountResponse = modelMapper.map(savedAccount, AccountDto.CreateAccountResponse.class);

        return ResponseEntity.ok().body(createAccountResponse);
    }
}