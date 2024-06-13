package com.sjdhome.todobackend.user;

import java.util.Map;

public interface UserController {
    void register(Map<String, Object> payload);
    String login(Map<String, Object> payload);
    void logout(String token);
    void authenticate(String token);
    String getUsername(String token);
}
