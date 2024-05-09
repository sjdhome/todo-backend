package com.sjdhome.todobackend.todo;

import com.sjdhome.todobackend.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends BaseRepository<Todo, Long> {

}
