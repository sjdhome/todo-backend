package com.sjdhome.todobackend.tag;

import com.sjdhome.todobackend.todo.Todo;
import com.sjdhome.todobackend.todo.TodoRepository;
import com.sjdhome.todobackend.user.TokenFactory;
import com.sjdhome.todobackend.user.User;
import com.sjdhome.todobackend.user.security.RequiresAuthorization;
import com.sjdhome.todobackend.user.security.UserSecurityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/tag")
public class TagControllerImpl implements TagController {
    private final TagRepository tagRepository;
    private final UserSecurityService userSecurityService;
    private final TodoRepository todoRepository;

    public TagControllerImpl(TagRepository tagRepository,
                             UserSecurityService userSecurityService,
                             TodoRepository todoRepository
    ) {
        this.tagRepository = tagRepository;
        this.userSecurityService = userSecurityService;
        this.todoRepository = todoRepository;
    }

    @Override
    @RequiresAuthorization
    @GetMapping("/todo/{todoId}")
    public List<Tag> getTagsByTodoId(@RequestHeader("Authorization") String token, @PathVariable long todoId) {
        User user = userSecurityService.authenticate(TokenFactory.from(token));
        if (!todoRepository.checkPermission(todoId, user.id())) {
            throw new SecurityException("User does not have access to todo");
        }
        Optional<Todo> todo = todoRepository.findById(todoId);
        if (todo.isEmpty()) {
            throw new NoSuchElementException("Todo not found");
        }
        return tagRepository.findAllByTodoId(todoId);
    }

    @Override
    @RequiresAuthorization
    @PostMapping("/todo/{todoId}")
    public void setTagsByTodoId(@RequestHeader("Authorization") String token, @PathVariable long todoId, @RequestBody List<String> tags) {
        User user = userSecurityService.authenticate(TokenFactory.from(token));
        if (!todoRepository.checkPermission(todoId, user.id())) {
            throw new SecurityException("User does not have access to todo");
        }
        tagRepository.deleteAllByTodoId(todoId);
        for (String tag : tags) {
            tagRepository.insert(new Tag(todoId, tag));
        }
    }
}
