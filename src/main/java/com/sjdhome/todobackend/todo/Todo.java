package com.sjdhome.todobackend.todo;

import java.sql.Timestamp;

public record Todo(Long id, Long categoryId, String title, String description, Boolean completed, Timestamp datetime, Boolean flagged) {
}
