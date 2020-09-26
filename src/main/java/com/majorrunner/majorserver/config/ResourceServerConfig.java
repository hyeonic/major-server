package com.majorrunner.majorserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

// 리소스 서버에 대한 설정
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("post");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .anonymous()
                .and()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.POST,"/api/account/signup")
                .permitAll()
                .mvcMatchers(HttpMethod.GET, "/api/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}
