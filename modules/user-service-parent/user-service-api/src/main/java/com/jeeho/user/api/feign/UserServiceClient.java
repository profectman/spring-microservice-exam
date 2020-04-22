package com.jeeho.user.api.feign;

import com.jeeho.common.basic.vo.UserVo;
import com.jeeho.common.core.constant.ServiceConstant;
import com.jeeho.common.core.model.ResponseBean;
import com.jeeho.common.feign.config.CustomFeignConfig;
import com.jeeho.user.api.feign.factory.UserServiceClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务
 */
@FeignClient(value = ServiceConstant.USER_SERVICE,
        configuration = CustomFeignConfig.class,
        fallbackFactory = UserServiceClientFallbackFactory.class)
public interface UserServiceClient {
    /**
     * 根据用户名获取用户信息
     * @param identifier
     * @param tenantCode
     * @return
     */
    @GetMapping("/v1/user/anonymousUser/findUserByIdentifier/{identifier}")
    ResponseBean<UserVo> findUserByIdentifier(@PathVariable("identifier") String identifier,
                                              @RequestParam("tenantCode") String tenantCode);

    /**
     * 根据用户名获取用户信息
     * @param identifier
     * @param identityType
     * @param tenantCode
     * @return
     */
    @GetMapping("/v1/user/anonymousUser/findUserByIdentifier/{identifier}")
    ResponseBean<UserVo> findUserByIdentifier(@PathVariable("identifier") String identifier,
                                              @RequestParam(value = "identityType", required = false) Integer identityType,
                                              @RequestParam("tenantCode") String tenantCode);

}
