package com.jeeho.user.api.feign.factory;

import com.jeeho.user.api.feign.UserServiceClient;
import com.jeeho.user.api.feign.fallback.UserServiceClientFallbackImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UserServiceClientFallbackFactory implements FallbackFactory<UserServiceClient> {
    @Override
    public UserServiceClient create(Throwable throwable) {
        UserServiceClientFallbackImpl clientFallback = new UserServiceClientFallbackImpl();
        clientFallback.setThrowable(throwable);
        return clientFallback;
    }
}
