package com.springboot.cloud.config;

import com.springboot.cloud.common.core.Interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Component
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    TokenInterceptor tokenInterceptor;

    String[] excludeUrl = {
            //临时
            "/member/saveMember",
            "/work/saveWorkList",
            "/timesheet/authorized",

            "/signin",
            "/signout",
            "/member/register",
            "/member/getUserList",
            "/work/test",
            "/authorized",
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/index.html", "/webjars/**", "/swagger-resources", "/v2/*", "/doc.html", "/app/**", "/assets/**", "/lib/**", "/views/**", "/template/**")
                .excludePathPatterns(excludeUrl);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
