package com.majorrunner.majorserver.config;

import com.majorrunner.majorserver.account.Account;
import com.majorrunner.majorserver.common.AppProperties;
import com.majorrunner.majorserver.account.AccountRole;
import com.majorrunner.majorserver.account.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Autowired
            AppProperties appProperties;

            @Override
            public void run(ApplicationArguments args) throws Exception {

                Set<AccountRole> adminAccountRoles = new HashSet<>();
                adminAccountRoles.add(AccountRole.ADMIN);
                adminAccountRoles.add(AccountRole.USER);

                Account admin = Account.createAccount(
                        appProperties.getAdminUserName(),
                        appProperties.getAdminPassword(),
                        "admin",
                        adminAccountRoles
                );
                accountService.saveAccount(admin);

                Set<AccountRole> accountRoles = new HashSet<>();
                adminAccountRoles.add(AccountRole.USER);

                Account user = Account.createAccount(
                        appProperties.getUserUsername(),
                        appProperties.getUserPassword(),
                        "user",
                        accountRoles
                );
                accountService.saveAccount(user);
            }
        };
    }

}
