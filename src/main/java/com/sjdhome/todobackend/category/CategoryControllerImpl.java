package com.sjdhome.todobackend.category;

import com.sjdhome.todobackend.user.TokenFactory;
import com.sjdhome.todobackend.user.User;
import com.sjdhome.todobackend.user.security.RequiresAuthorization;
import com.sjdhome.todobackend.user.security.UserSecurityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryControllerImpl implements CategoryController {
    private final UserSecurityService userSecurityService;
    private final CategoryRepository categoryRepository;

    public CategoryControllerImpl(UserSecurityService userSecurityService,
                                  CategoryRepository categoryRepository) {
        this.userSecurityService = userSecurityService;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @RequiresAuthorization
    @PostMapping("/")
    public long create(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> payload) {
        User user = userSecurityService.authenticate(TokenFactory.from(token));
        String title = (String) payload.get("title");
        if (title == null) {
            throw new IllegalArgumentException("title is required");
        }
        Category category = new Category(null, user.id(), title);
        return categoryRepository.insert(category);
    }

    @Override
    @RequiresAuthorization
    @PutMapping("/{id}")
    public void update(@RequestHeader("Authorization") String token, @PathVariable long id, @RequestBody Map<String, Object> payload) {
        User user = userSecurityService.authenticate(TokenFactory.from(token));
        String title = (String) payload.get("title");
        if (title == null) {
            throw new IllegalArgumentException("title is required");
        }
        Category category = new Category(id, user.id(), title);
        categoryRepository.update(id, category);
    }

    @Override
    @RequiresAuthorization
    @GetMapping("/{id}")
    public Category getCategoryById(@RequestHeader("Authorization") String token, @PathVariable long id) {
        User user = userSecurityService.authenticate(TokenFactory.from(token));
        if (!categoryRepository.checkPermission(id, user.id())) {
            throw new SecurityException("User does not have access to category");
        }
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new NoSuchElementException("Category not found");
        }
        return category.get();
    }

    @Override
    @GetMapping("/")
    @RequiresAuthorization
    public List<Category> getAllCategories(@RequestHeader("Authorization") String token) {
        User user = userSecurityService.authenticate(TokenFactory.from(token));
        return categoryRepository.findAllByUserId(user.id());
    }

    @Override
    @DeleteMapping("/{id}")
    @RequiresAuthorization
    public void deleteCategoryById(@RequestHeader("Authorization") String token, @PathVariable long id) {
        User user = userSecurityService.authenticate(TokenFactory.from(token));
        if (!categoryRepository.checkPermission(id, user.id())) {
            throw new SecurityException("User does not have access to category");
        }
        categoryRepository.deleteById(id);
    }
}
