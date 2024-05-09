package com.sjdhome.todobackend.user;

import java.util.Optional;

public interface UserService {
    void addUser(User user);
    Optional<User> getUserByUsername(String username);
    void modifyNickname(long id, String nickname);
    void removeUser(User user);
}
