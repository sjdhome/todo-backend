package com.sjdhome.todobackend.user;

import com.sjdhome.todobackend.user.security.RequiresAuthorization;
import com.sjdhome.todobackend.user.security.Token;
import com.sjdhome.todobackend.user.security.UserSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/user")
public class UserControllerImpl implements UserController {
    private static final Logger log = LoggerFactory.getLogger(UserControllerImpl.class);
    private final UserSecurityService userSecurityService;
    private final UserRepository userRepository;

    public UserControllerImpl(UserSecurityService userSecurityService, UserRepository userRepository) {
        this.userSecurityService = userSecurityService;
        this.userRepository = userRepository;
    }

    @Override
    @PostMapping("/register")
    @Transactional
    public void register(@RequestBody Map<String, Object> payload) {
        String username = (String) payload.get("username");
        String password = (String) payload.get("password");
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username or password is null");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserExistsException();
        }
        userSecurityService.register(username, password);
    }

    @Override
    @PostMapping("/login")
    public String login(@RequestBody Map<String, Object> payload) {
        String username = (String) payload.get("username");
        String password = (String) payload.get("password");
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username or password is null");
        }
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new InvalidUserException(username);
        }
        Token token = userSecurityService.login(user.get().id(), password);
        return token.token();
    }

    @Override
    @PostMapping("/logout")
    @RequiresAuthorization
    public void logout(@RequestHeader("Authorization") String token) {
        userSecurityService.logout(new Token(token.substring(7)));
    }

    @Override
    @GetMapping("/authenticate")
    @RequiresAuthorization
    public void authenticate(@RequestHeader("Authorization") String token) {
        userSecurityService.authenticate(new Token(token.substring(7)));
    }

    @Override
    @GetMapping("/username")
    @RequiresAuthorization
    public String getUsername(@RequestHeader("Authorization") String token) {
        final User user = userSecurityService.authenticate(new Token(token.substring(7)));
        return user.username();
    }
}
