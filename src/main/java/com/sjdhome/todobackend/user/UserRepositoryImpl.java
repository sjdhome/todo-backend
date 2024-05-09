package com.sjdhome.todobackend.user;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JdbcClient jdbcClient;

    public UserRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<User> findById(Long id) {
        return jdbcClient
                .sql("SELECT * FROM user WHERE id=:id")
                .param("id", id)
                .query(User.class)
                .optional();
    }

    @Override
    public List<User> findAll() {
        return jdbcClient
                .sql("SELECT * FROM user")
                .query(User.class)
                .list();
    }

    @Override
    public User save(User obj) {
        if (findById(obj.id()).isPresent()) {
            jdbcClient
                    .sql("UPDATE user SET nickname=:nickname WHERE id=:id")
                    .paramSource(obj)
                    .update();
        } else {
            jdbcClient
                    .sql("INSERT INTO user(username, nickname) VALUES (:username,:nickname)")
                    .paramSource(obj)
                    .update();
        }
        return obj;
    }

    @Override
    public void deleteById(Long id) {
        jdbcClient
                .sql("DELETE FROM user WHERE id=:id")
                .param("id", id)
                .update();
    }

    @Override
    public void delete(User obj) {
        jdbcClient
                .sql("DELETE FROM user WHERE id=:id")
                .paramSource(obj)
                .update();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcClient
                .sql("SELECT * FROM user WHERE username=:username")
                .param("username", username)
                .query(User.class)
                .optional();
    }
}
