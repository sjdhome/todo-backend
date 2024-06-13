package com.sjdhome.todobackend.user.security;

import com.sjdhome.todobackend.user.InvalidUserException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    private final UserSecurityService userSecurityService;

    public AuthorizationInterceptor(UserSecurityService userSecurityService) {
        this.userSecurityService = userSecurityService;
    }

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            RequiresAuthorization annotation = handlerMethod.getMethodAnnotation(RequiresAuthorization.class);
            if (annotation != null) {
                String authorizationHeader = request.getHeader("Authorization");
                if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return false;
                }
                String token = authorizationHeader.substring("Bearer ".length());
                try {
                    userSecurityService.authenticate(new Token(token));
                } catch (InvalidTokenException | InvalidUserException e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return false;
                }
            }
        }
        return true;
    }
}
