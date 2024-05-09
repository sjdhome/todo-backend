package com.sjdhome.todobackend.security;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserSecurityRepositoryImpl implements UserSecurityRepository {
    private final JdbcClient jdbcClient;

    public UserSecurityRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<UserSecurity> findById(String username) {
        return jdbcClient
                .sql("SELECT * FROM user_security WHERE username=:username")
                .param("username", username)
                .query(UserSecurity.class)
                .optional();
    }

    @Override
    public List<UserSecurity> findAll() {
        return jdbcClient
                .sql("SELECT * FROM user_security")
                .query(UserSecurity.class)
                .list();
    }

    @Override
    public UserSecurity save(UserSecurity obj) {
        if (findById(obj.username()).isPresent()) {
            jdbcClient
                    .sql("UPDATE user_security SET encoded_password=:encoded_password WHERE username=:username")
                    .paramSource(obj)
                    .update();
        } else {
            jdbcClient
                    .sql("INSERT INTO user_security VALUES (:username, :encoded_password)")
                    .paramSource(obj)
                    .update();
        }
        return obj;
    }

    @Override
    public void deleteById(String username) {
        jdbcClient
                .sql("DELETE FROM user_security WHERE username=:username")
                .param("username", username)
                .update();
    }

    @Override
    public void delete(UserSecurity obj) {
        jdbcClient
                .sql("DELETE FROM user_security WHERE username=:username")
                .paramSource(obj)
                .update();
    }
}
