package com.sjdhome.todobackend;

import com.sjdhome.todobackend.user.InvalidUserException;
import com.sjdhome.todobackend.user.UserExistsException;
import com.sjdhome.todobackend.user.security.InvalidPasswordException;
import com.sjdhome.todobackend.user.security.InvalidTokenException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({
            InvalidUserException.class,
            InvalidTokenException.class,
            AuthenticationException.class,
            InvalidPasswordException.class,
            UserExistsException.class
    })
    public ResponseEntity<String> handlePermissionException(Exception e) {
        return ResponseEntity.status(401).body(e.toString());
    }

    @ExceptionHandler({IllegalArgumentException.class,})
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(400).body(e.toString());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(404).body(e.toString());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(500).body(e.toString());
    }
}
