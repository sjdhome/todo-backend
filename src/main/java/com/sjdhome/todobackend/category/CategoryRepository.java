package com.sjdhome.todobackend.category;

import com.sjdhome.todobackend.BaseRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class CategoryRepository extends BaseRepository<Category, Long> {
    public CategoryRepository(JdbcClient jdbcClient) {
        super(jdbcClient, "category");
    }

    @Override
    public Long insert(Category category) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient
                .sql("INSERT INTO category (user_id, title) VALUES (?, ?)")
                .param(category.userId())
                .param(category.title())
                .update(keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void update(Long id, Category category) {
        jdbcClient
                .sql("UPDATE category SET user_id = ?, title = ? WHERE id = ?")
                .param(category.userId())
                .param(category.title())
                .param(id)
                .update();
    }

    public List<Category> findAllByUserId(long userId) {
        return jdbcClient
                .sql("SELECT * FROM category WHERE user_id = ?")
                .param(userId)
                .query(Category.class)
                .list();
    }

    public boolean checkPermission(long categoryId, long userId) {
        return jdbcClient
                .sql("SELECT COUNT(*) FROM category WHERE id = ? AND user_id = ?")
                .param(categoryId)
                .param(userId)
                .query(Integer.class)
                .single() > 0;
    }
}
