package com.majorrunner.majorserver.config;

import com.majorrunner.majorserver.account.AccountService;
import com.majorrunner.majorserver.common.AppProperties;
import com.majorrunner.majorserver.common.TestDecription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthServerConfigTest {

    @Autowired protected MockMvc mockMvc;
    @Autowired AccountService accountService;
    @Autowired AppProperties appProperties;

    @Test
    @TestDecription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception {

        this.mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                    .param("username", appProperties.getUserUsername())
                    .param("password", appProperties.getUserPassword())
                    .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }
}