package com.sjdhome.todobackend.todo;

import com.sjdhome.todobackend.BaseRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class TodoRepository extends BaseRepository<Todo, Long> {
    public TodoRepository(JdbcClient jdbcClient) {
        super(jdbcClient, "todo");
    }
    
    @Override
    public Long insert(Todo todo) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient
                .sql("INSERT INTO todo (category_id, title, description, completed, datetime, flagged) VALUES (:category_id, :title, :description, :completed, :datetime, :flagged)")
                .param("category_id", todo.categoryId())
                .param("title", todo.title())
                .param("description", todo.description())
                .param("completed", todo.completed())
                .param("datetime", todo.datetime())
                .param("flagged", todo.flagged())
                .update(keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void update(Long id, Todo todo) {
        jdbcClient
                .sql("UPDATE todo SET category_id=:category_id, title=:title, description=:description, completed=:completed, datetime=:datetime, flagged=:flagged WHERE id=:id")
                .param("category_id", todo.categoryId())
                .param("id", id)
                .param("title", todo.title())
                .param("description", todo.description())
                .param("completed", todo.completed())
                .param("datetime", todo.datetime())
                .param("flagged", todo.flagged())
                .update();
    }

    public List<Todo> findAllByCategoryId(long categoryId) {
        return jdbcClient
                .sql("SELECT * FROM todo WHERE category_id = :category_id")
                .param("category_id", categoryId)
                .query(Todo.class)
                .list();
    }

    public boolean checkPermission(long todoId, long userId) {
        long categoryId = jdbcClient
                .sql("SELECT category_id FROM todo WHERE id = :id")
                .param("id", todoId)
                .query(Long.class)
                .single();
        return userId == jdbcClient
                .sql("SELECT user_id FROM category WHERE id = :id")
                .param("id", categoryId)
                .query(Long.class)
                .single();
    }
}
