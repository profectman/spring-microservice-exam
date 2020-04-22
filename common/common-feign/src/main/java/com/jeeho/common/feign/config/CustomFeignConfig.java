package com.jeeho.common.feign.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * feign的Request拦截器，拦截请求并向request中添加对应参数
 */
@Configuration
public class CustomFeignConfig implements RequestInterceptor {

    private static final String TOKEN_HEADER = "authorization";
    private static final String TENANT_HEADER = "Tenant-Code";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest request = getHttpServletRequest();
        if (null != request){
            requestTemplate.header(TOKEN_HEADER,getHeaders(request).get(TOKEN_HEADER));
            requestTemplate.header(TENANT_HEADER,getHeaders(request).get(TENANT_HEADER));
        }
    }

    /**
     * 从requestContextHolder中获取到HttpServletRequest
     * @return
     */
    private HttpServletRequest getHttpServletRequest(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        try {
            if (servletRequestAttributes != null)
                return servletRequestAttributes.getRequest();
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从请求头中获取到信息存放到Map中
     * @param request
     * @return
     */
    private Map<String,String> getHeaders(HttpServletRequest request){
        Map<String,String> map = new LinkedHashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key,value);
        }
        return map;
    }
}
