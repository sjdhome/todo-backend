package com.sjdhome.todobackend.user;

import com.sjdhome.todobackend.BaseRepository;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
