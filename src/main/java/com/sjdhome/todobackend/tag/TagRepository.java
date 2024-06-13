package com.sjdhome.todobackend.tag;

import com.sjdhome.todobackend.BaseRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class TagRepository extends BaseRepository<Tag, Long> {
    public TagRepository(JdbcClient jdbcClient) {
        super(jdbcClient, "tag");
    }
    
    @Override
    public Long insert(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient
                .sql("INSERT INTO tag (todo_id, tag) VALUES (:todo_id, :tag)")
                .param("todo_id", tag.todoId())
                .param("tag", tag.tag())
                .update(keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void update(Long id, Tag tag) {
        jdbcClient
                .sql("UPDATE tag SET todo_id=:todo_id, tag=:tag WHERE id=:id")
                .param("todo_id", tag.todoId())
                .param("tag", tag.tag())
                .update();
    }

    public List<Tag> findAllByTodoId(long todoId) {
        return jdbcClient
                .sql("SELECT * FROM tag WHERE todo_id = :todo_id")
                .param("todo_id", todoId)
                .query(Tag.class)
                .list();
    }

    public void deleteAllByTodoId(long todoId) {
        jdbcClient
                .sql("DELETE FROM tag WHERE todo_id = :todo_id")
                .param("todo_id", todoId)
                .update();
    }

    public List<Long> findAllTodoIdsByTag(String tag) {
        return jdbcClient
                .sql("SELECT todo_id FROM tag WHERE tag = :tag")
                .param("tag", tag)
                .query(Long.class)
                .list();
    }
}
