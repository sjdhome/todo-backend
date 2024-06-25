package com.sjdhome.todobackend;

import com.sjdhome.todobackend.user.security.AuthorizationInterceptor;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthorizationInterceptor authorizationInterceptor;

    public WebMvcConfig(AuthorizationInterceptor authorizationInterceptor) {
        this.authorizationInterceptor = authorizationInterceptor;
    }

    @Override
    public void addInterceptors(@Nonnull InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
        registry.addMapping("/**")
                .allowedOrigins("https://todo.sjdhome.com", "http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
