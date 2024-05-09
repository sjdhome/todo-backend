package com.sjdhome.todobackend;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
    ID save(T obj);
    void deleteById(ID id);
    void delete(T obj);
}
