package com.jeeho.common.core.utils;

import com.jeeho.common.core.expcetions.CommonException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 以静态变量的方式存储ApplicationContext
 */
@Service
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {
    private static ApplicationContext applicationContext = null;
    @Override
    public void destroy() throws Exception {
        applicationContext = null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * 获取ApplicationContext
     * @return
     */
    public static ApplicationContext getApplicationContext(){
        if (applicationContext == null)
            throw new CommonException("applicationContext为空！");
        return applicationContext;
    }

    /**
     * 事件发布
     * @param event
     */
    public static void publishEvent(ApplicationEvent event){
        if (applicationContext == null)
            return;
        applicationContext.publishEvent(event);
    }
}
