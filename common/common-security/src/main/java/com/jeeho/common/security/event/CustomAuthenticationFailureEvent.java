package com.jeeho.common.security.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class CustomAuthenticationFailureEvent extends ApplicationEvent {

    private UserDetails userDetails;

    public CustomAuthenticationFailureEvent(Authentication authentication, UserDetails userDetails) {
        super(authentication);
        this.userDetails = userDetails;
    }

}
