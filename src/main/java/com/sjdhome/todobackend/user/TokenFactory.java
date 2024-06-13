package com.sjdhome.todobackend.user;

import com.sjdhome.todobackend.user.security.Token;

public class TokenFactory {
    public static Token from(String token) {
        return new Token(token.substring(7));
    }
}
