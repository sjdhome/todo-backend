package com.sjdhome.todobackend.user;

import com.sjdhome.todobackend.BaseRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User, Long> {
    public UserRepository(JdbcClient jdbcClient) {
        super(jdbcClient, "user");
    }
    
    public Optional<User> findByUsername(String username) {
        return jdbcClient
                .sql("SELECT * FROM user WHERE username=:username")
                .param("username", username)
                .query(User.class)
                .optional();
    }

    @Override
    public Long insert(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient
                .sql("INSERT INTO user (username, encoded_password) VALUES (:username, :encoded_password)")
                .param("username", user.username())
                .param("encoded_password", user.encodedPassword())
                .update(keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void update(Long id, User user) {
        jdbcClient
                .sql("UPDATE user SET username=:username, encoded_password=:encoded_password WHERE id=:id")
                .param("id", id)
                .param("username", user.username())
                .param("encoded_password", user.encodedPassword())
                .update();
    }
}
