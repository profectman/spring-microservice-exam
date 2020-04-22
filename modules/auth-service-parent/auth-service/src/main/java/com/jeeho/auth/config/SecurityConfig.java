package com.jeeho.auth.config;

import com.jeeho.auth.security.CustomUserDetailsAuthenticationProvider;
import com.jeeho.common.security.core.CustomUserDetailsService;
import com.jeeho.common.security.error.CustomOAuth2AccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * spring seucurity安全认证配置类
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthorizationServerEndpointsConfiguration endpoints;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(authProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated();
        //权限校验处理类
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler());
    }
    /**
     * 自定义OAuth2鉴权失败处理类
     * @return
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomOAuth2AccessDeniedHandler();
    }

    /**
     * 认证信息处理类
     * @return
     */
    @Bean
    public AuthenticationProvider authProvider() {
        return new CustomUserDetailsAuthenticationProvider(customUserDetailsService,encoder());
    }
    /**
     * 密码加密
     * @return
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
