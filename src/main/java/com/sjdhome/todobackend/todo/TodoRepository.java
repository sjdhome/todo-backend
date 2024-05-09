package com.sjdhome.todobackend.todo;

import com.sjdhome.todobackend.BaseRepository;
import com.sjdhome.todobackend.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends BaseRepository<Todo, Long> {
    List<Todo> getTodosByUser(User user);
    void bindUser(Todo todo, User user);
}
