package com.sjdhome.todobackend.user;

public record User(Long id, String username, String encodedPassword) {
}
