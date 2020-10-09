package com.majorrunner.majorserver.account;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Account saveAccount(Account account) {

        Set<AccountRole> accountRoles = new HashSet<>();
        accountRoles.add(AccountRole.USER);

        Account createdAccount = Account.createAccount(account.getUsername(),
                                                        account.getPassword(),
                                                        account.getNickName(),
                                                        accountRoles);

        createdAccount.setPassword(this.passwordEncoder.encode(createdAccount.getPassword()));
        return accountRepository.save(createdAccount);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new AccountAdapter(account);
    }
}
