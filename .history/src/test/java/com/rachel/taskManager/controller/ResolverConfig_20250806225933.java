package com.rachel.taskManager.controller;

import java.util.List;

import com.rachel.taskManager.model.User;
import com.rachel.taskManager.security.CurrentUser;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@TestConfiguration class ResolverConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.getParameterAnnotation(CurrentUser.class) != null;
            }

            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                          NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                User mockUser = new User();
                mockUser.setId(1L);
                mockUser.setEmail("test@example.com");
                return mockUser;
            }
        });
    }
}

