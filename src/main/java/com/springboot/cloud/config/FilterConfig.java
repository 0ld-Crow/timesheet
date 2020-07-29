package com.springboot.cloud.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;

/**
 * 
 * @ClassName FilterConfig  
 * @Description 过滤器配置
 *
 */
@Configuration
public class FilterConfig {

    /**
     利用FilterRegistrationBean和DelegatingFilterProxy把过滤器shiro注册进spring容器中
     */
    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> delegatingFilterProxy() {
        FilterRegistrationBean<DelegatingFilterProxy> registration = new FilterRegistrationBean<DelegatingFilterProxy>();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();

        proxy.setTargetBeanName("shiroFilter");
        //如果设置"targetFilterLifecycle"为True，则spring来管理Filter.init()和Filter.destroy()；
        //若为false，则这两个方法失效！！
        proxy.setTargetFilterLifecycle(true);
        //过滤器拦截的顺序（0是处于首位）
        registration.setOrder(0);
        registration.setFilter(proxy);
        //过滤器生效的范围
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR, DispatcherType.ASYNC);
        return registration;
    }
}