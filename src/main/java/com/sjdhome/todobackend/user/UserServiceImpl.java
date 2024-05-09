package com.sjdhome.todobackend.user;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void modifyNickname(long id, String nickname) {
        userRepository.findById(id)
                .ifPresent(user -> userRepository.save(new User(user.id(), user.username(), nickname)));
    }

    @Override
    public void removeUser(User user) {
        userRepository.delete(user);
    }
}
