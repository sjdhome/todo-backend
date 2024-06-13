package com.sjdhome.todobackend.todo;

import java.util.List;
import java.util.Map;

public interface TodoController {
    List<Todo> getAllTodos(String token);
    Todo getTodoById(String token, long id);
    List<Todo> getTodosByCategoryId(String token, long categoryId);
    List<Todo> getTodosByTagId(String token, String tag);
    void deleteTodoById(String token, long id);
    long insert(String token, long categoryId, Map<String, Object> payload);
    long update(String token, long id, Map<String, Object> payload);
}
