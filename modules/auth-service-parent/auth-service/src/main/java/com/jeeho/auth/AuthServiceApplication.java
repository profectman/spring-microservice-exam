package com.jeeho.auth;

import com.jeeho.common.core.constant.CommonConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication(scanBasePackages = {CommonConstant.BASE_PACKAGE})
@EnableDiscoveryClient
@EnableAuthorizationServer
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class,args);
    }
}
