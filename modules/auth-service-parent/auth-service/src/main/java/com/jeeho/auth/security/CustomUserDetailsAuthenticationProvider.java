package com.jeeho.auth.security;

import com.jeeho.common.core.utils.SpringContextHolder;
import com.jeeho.common.security.core.CustomUserDetailsService;
import com.jeeho.common.security.event.CustomAuthenticationFailureEvent;
import com.jeeho.common.security.event.CustomAuthenticationSuccessEvent;
import com.jeeho.common.security.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
@Slf4j
public class CustomUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    //userDetail
    CustomUserDetailsService userDetailsService;
    //密码加密
    PasswordEncoder passwordEncoder;

    private String userNotFoundEncodedPassword;

    public CustomUserDetailsAuthenticationProvider(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 如果用户信息注册失败
     * @throws Exception
     */
    @Override
    protected void doAfterPropertiesSet() throws Exception {
        if (this.userDetailsService == null)
            throw new IllegalArgumentException("UserDetailsService can not be null");
        this.userNotFoundEncodedPassword = this.passwordEncoder.encode("userNotFoundPassword");
    }

    /**
     * 检查用户信息
     * @param userDetails
     * @param usernamePasswordAuthenticationToken
     * @throws AuthenticationException
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        //判断用户输入认证信息是否为空
        if (usernamePasswordAuthenticationToken.getCredentials() == null){
            log.debug("Authentication failed: password is blank");
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "密码为空"));
        }
        //获取密码
        String presentedPassword = usernamePasswordAuthenticationToken.getCredentials().toString();
        //匹配密码
        if (!passwordEncoder.matches(userDetails.getPassword(),presentedPassword)){
            log.debug("Authentication failed: invalid password");
            //发布认证用户信息失败
            SpringContextHolder.publishEvent(new CustomAuthenticationFailureEvent(usernamePasswordAuthenticationToken,userDetails));
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "用户名或密码错误"));
        }
        //匹配成功发布事件
        SpringContextHolder.publishEvent(new CustomAuthenticationSuccessEvent(usernamePasswordAuthenticationToken,userDetails));
    }

    /**
     * 注册用户信息
     * @param s
     * @param usernamePasswordAuthenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected UserDetails retrieveUser(String s, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        UserDetails loadedUser;
        try {
            loadedUser = this.userDetailsService.loadUserByIdentifierAndTenantCode(TenantContextHolder.getTenantCode(),
                    usernamePasswordAuthenticationToken.getPrincipal().toString());
        } catch (UsernameNotFoundException notFound) {
            if (usernamePasswordAuthenticationToken.getCredentials() != null) {
                String presentedPassword = usernamePasswordAuthenticationToken.getCredentials().toString();
                passwordEncoder.matches(presentedPassword, userNotFoundEncodedPassword);
            }
            throw notFound;
        } catch (Exception tenantNotFound) {
            throw new InternalAuthenticationServiceException(tenantNotFound.getMessage(), tenantNotFound);
        }
        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException("get user information failed");
        }
        return loadedUser;
    }
}
