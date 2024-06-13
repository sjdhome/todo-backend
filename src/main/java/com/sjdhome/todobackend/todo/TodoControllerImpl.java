package com.sjdhome.todobackend.todo;

import com.sjdhome.todobackend.category.Category;
import com.sjdhome.todobackend.category.CategoryRepository;
import com.sjdhome.todobackend.tag.TagRepository;
import com.sjdhome.todobackend.user.TokenFactory;
import com.sjdhome.todobackend.user.User;
import com.sjdhome.todobackend.user.security.RequiresAuthorization;
import com.sjdhome.todobackend.user.security.UserSecurityService;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/todo")
public class TodoControllerImpl implements TodoController {
    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UserSecurityService userSecurityService;

    public TodoControllerImpl(TodoRepository todoRepository,
                                CategoryRepository categoryRepository,
                                TagRepository tagRepository,
                              UserSecurityService userSecurityService
    ) {
        this.todoRepository = todoRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.userSecurityService = userSecurityService;
    }

    @Override
    @GetMapping("/")
    @RequiresAuthorization
    public List<Todo> getAllTodos(@RequestHeader("Authorization") String token) {
        User user = userSecurityService.authenticate(TokenFactory.from(token));
        List<Category> categories = categoryRepository.findAllByUserId(user.id());
        return categories
                .stream()
                .map(Category::id)
                .map(todoRepository::findAllByCategoryId)
                .flatMap(List::stream)
                .toList();
    }

    @Override
    @GetMapping("/{id}")
    @RequiresAuthorization
    public Todo getTodoById(@RequestHeader("Authorization") String token, @PathVariable long id) {
        User user = userSecurityService.authenticate(TokenFactory.from(token));
        if (!todoRepository.checkPermission(id, user.id())) {
            throw new SecurityException("User does not have access to todo");
        }
        Optional<Todo> todo = todoRepository.findById(id);
        if (todo.isEmpty()) {
            throw new NoSuchElementException("Todo not found");
        } else {
            return todo.get();
        }
    }

    @Override
    @GetMapping("/category/{categoryId}")
    @RequiresAuthorization
    public List<Todo> getTodosByCategoryId(@RequestHeader("Authorization") String token, @PathVariable long categoryId) {
        User user = userSecurityService.authenticate(TokenFactory.from(token));
        if (!categoryRepository.checkPermission(categoryId, user.id())) {
            throw new SecurityException("User does not have access to category");
        }
        return todoRepository.findAllByCategoryId(categoryId);
    }

    @Override
    @GetMapping("/tag/{tag}")
    @RequiresAuthorization
    public List<Todo> getTodosByTagId(@RequestHeader("Authorization") String token, @PathVariable String tag) {
        User user = userSecurityService.authenticate(TokenFactory.from(token));
        return tagRepository.findAllTodoIdsByTag(tag)
                .stream()
                .filter(todoId -> todoRepository.checkPermission(todoId, user.id()))
                .map(todoRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    @DeleteMapping("/{id}")
    @RequiresAuthorization
    public void deleteTodoById(@RequestHeader("Authorization") String token, @PathVariable long id) {
        User user = userSecurityService.authenticate(TokenFactory.from(token));
        if (!todoRepository.checkPermission(id, user.id())) {
            throw new SecurityException("User does not have access to todo");
        }
        todoRepository.deleteById(id);
    }

    @Override
    @PostMapping("/category/{categoryId}")
    @RequiresAuthorization
    public long insert(@RequestHeader("Authorization") String token, @PathVariable long categoryId, @RequestBody Map<String, Object> payload) {
        final User user = userSecurityService.authenticate(TokenFactory.from(token));
        if (!categoryRepository.checkPermission(categoryId, user.id())) {
            throw new SecurityException("User does not have access to category");
        }
        final String title = (String) payload.get("title");
        final String description = (String) payload.get("description");
        final Boolean completed = (Boolean) payload.get("completed");
        final Timestamp datetime;
        final Object _datetime = payload.get("datetime");
        if (_datetime == null) {
            datetime = null;
        } else {
            datetime = new Timestamp(Long.parseLong(Objects.toString(_datetime)));
        }
        final Boolean flagged = (Boolean) payload.get("flagged");
        if (title == null || description == null || completed == null || flagged == null) {
            String situation = String.format("%s, %s, %s, %s, %s", title, description, completed, flagged, categoryId);
            throw new IllegalArgumentException("Need full information to create a todo: " + situation);
        }
        Todo todo = new Todo(null, user.id(), title, description, completed, datetime, flagged);
        return todoRepository.insert(todo);
    }

    @Override
    @PutMapping("/{id}")
    @RequiresAuthorization
    public long update(@RequestHeader("Authorization") String token, @PathVariable long id, @RequestBody Map<String, Object> payload) {
        final User user = userSecurityService.authenticate(TokenFactory.from(token));
        if (!todoRepository.checkPermission(id, user.id())) {
            throw new SecurityException("User does not have access to todo");
        }
        final Todo oldTodo = todoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Todo not found"));
        final String title = Objects.requireNonNullElse((String) payload.get("title"), oldTodo.title());
        final String description = Objects.requireNonNullElse((String) payload.get("description"), oldTodo.description());
        final Boolean completed = Objects.requireNonNullElse((Boolean) payload.get("completed"), oldTodo.completed());
        final Timestamp datetime;
        final Object _datetime = payload.get("datetime");
        if (_datetime == null) {
            datetime = oldTodo.datetime();
        } else {
            datetime = new Timestamp(Long.parseLong(Objects.toString(_datetime)));
        }
        final Boolean flagged = Objects.requireNonNullElse((Boolean) payload.get("flagged"), oldTodo.flagged());
        Todo todo = new Todo(id, user.id(), title, description, completed, datetime, flagged);
        todoRepository.update(id, todo);
        return id;
    }
}
