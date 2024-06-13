package com.sjdhome.todobackend.user.security;

import com.sjdhome.todobackend.user.User;

public interface UserSecurityService {
    Token login(long userId, String password);
    void logout(Token token);
    void register(String username, String password);
    User authenticate(Token token);
}
