package com.sjdhome.todobackend.category;

import com.sjdhome.todobackend.todo.Todo;

import java.util.List;

public record Category(
        long id,
        String title,
        List<Todo> todos
) {
}
