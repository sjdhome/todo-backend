package com.sjdhome.todobackend.category;

import java.util.List;
import java.util.Map;

public interface CategoryController {
    long create(String token, Map<String, Object> payload);
    void update(String token, long id, Map<String, Object> payload);
    Category getCategoryById(String token, long id);
    List<Category> getAllCategories(String token);
    void deleteCategoryById(String token, long id);
}
