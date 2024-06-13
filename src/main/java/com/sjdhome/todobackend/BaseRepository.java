package com.sjdhome.todobackend;

import org.springframework.jdbc.core.simple.JdbcClient;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T, ID> {
    protected final JdbcClient jdbcClient;
    protected final String tableName;

    public BaseRepository(JdbcClient jdbcClient, String tableName) {
        this.jdbcClient = jdbcClient;
        this.tableName = tableName;
    }

    private Class<T> getTClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Optional<T> findById(ID id) {
        return jdbcClient.sql("SELECT * FROM `" + tableName + "` WHERE id=:id")
                .param("id", id)
                .query(getTClass())
                .optional();
    }

    public List<T> findAll() {
        return jdbcClient.sql("SELECT * FROM `" + tableName + "`")
                .param("table_name", tableName)
                .query(getTClass())
                .list();
    }

    public ID save(ID id, T obj) {
        if (exist(id)) {
            update(id, obj);
            return id;
        } else {
            return insert(obj);
        }
    }

    public boolean exist(ID id) {
        return jdbcClient.sql("SELECT COUNT(*) FROM `" + tableName + "` WHERE id=:id")
                .param("id", id)
                .query(Integer.class)
                .single() == 1;
    }

    public abstract ID insert(T obj);

    public abstract void update(ID id, T obj);

    public void deleteById(ID id) {
        jdbcClient.sql("DELETE FROM `" + tableName + "` WHERE id=:id")
                .param("id", id)
                .param("table_name", tableName)
                .update();
    }
}
