package com.sjdhome.todobackend.todo;

import java.util.Date;

public record Todo(
        long id,
        String title,
        String description,
        boolean completed,
        Date datetime,
        boolean flagged
) {
}
