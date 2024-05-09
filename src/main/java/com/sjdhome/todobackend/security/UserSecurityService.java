package com.sjdhome.todobackend.security;

import com.sjdhome.todobackend.user.InvalidUserException;

public interface UserSecurityService {
    Token login(String username, String password) throws AuthenticationException, InvalidUserException, UserHasNoSecurityMethodsException;

    void logout(Token token) throws InvalidTokenException;

    void register(String username, String password) throws InvalidUserException, PasswordExistsException;

    void authenticate(Token token) throws InvalidTokenException;
}
