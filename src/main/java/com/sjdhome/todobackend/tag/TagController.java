package com.sjdhome.todobackend.tag;

import java.util.List;

public interface TagController {
    List<Tag> getTagsByTodoId(String token, long todoId);
    void setTagsByTodoId(String token, long todoId, List<String> tags);
}
