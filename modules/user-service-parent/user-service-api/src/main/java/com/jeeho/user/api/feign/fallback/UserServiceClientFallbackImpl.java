package com.jeeho.user.api.feign.fallback;

import com.jeeho.common.basic.vo.UserVo;
import com.jeeho.common.core.model.ResponseBean;
import com.jeeho.user.api.feign.UserServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserServiceClientFallbackImpl implements UserServiceClient {

    private Throwable throwable;

    /**
     * 根据用户名获取用户信息
     *
     * @param identifier
     * @param tenantCode
     * @return
     */
    @Override
    public ResponseBean<UserVo> findUserByIdentifier(String identifier, String tenantCode) {
        log.error("Feign findUserByIdentifier failed: {}, {}, {}", tenantCode, identifier, throwable);
        return null;
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param identifier
     * @param identityType
     * @param tenantCode
     * @return
     */
    @Override
    public ResponseBean<UserVo> findUserByIdentifier(String identifier, Integer identityType, String tenantCode) {
        log.error("Feign findUserByIdentifier failed: {}, {}, {}, {}", tenantCode, identityType, identifier, throwable);
        return null;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
