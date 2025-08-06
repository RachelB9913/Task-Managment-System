package com.rachel.taskManager.controller;

import com.rachel.taskManager.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.core.MethodParameter;

@Configuration
public class MockCurrentUserResolverConfig {
    @Bean
    public HandlerMethodArgumentResolver mockCurrentUserArgumentResolver() {
        return new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.getParameterType().equals(User.class);
            }

            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                         NativeWebRequest webRequest, org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {
                User user = new User();
                user.setCognitoSub("mock-cognito-sub");
                user.setMail("mockuser@example.com");
                user.setAdmin(false);
                return user;
            }
        };
    }
}
