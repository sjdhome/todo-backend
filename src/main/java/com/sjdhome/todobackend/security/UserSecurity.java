package com.sjdhome.todobackend.security;

public record UserSecurity(
        String username,
        String encodedPassword
) {
}