package com.sjdhome.todobackend.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void addAndRemoveTest() {
        userService.addUser(new User(0, "test", "test"));
        var user = userService.getUserByUsername("test");
        Assertions.assertTrue(user.isPresent());
        userService.modifyNickname(user.get().id(), "test2");
        user = userService.getUserByUsername("test");
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals("test2", user.get().nickname());
        userService.removeUser(user.get());
        user = userService.getUserByUsername("test");
        Assertions.assertTrue(user.isEmpty());
    }
}
