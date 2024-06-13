package com.sjdhome.todobackend.user.security;

import com.sjdhome.todobackend.user.InvalidUserException;
import com.sjdhome.todobackend.user.User;
import com.sjdhome.todobackend.user.UserExistsException;
import com.sjdhome.todobackend.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserSecurityServiceImpl implements UserSecurityService {
    private final Map<Token, Long> tokens;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSecurityServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.tokens = new HashMap<>();
        this.userRepository = userRepository;
    }

    @Override
    public Token login(long userId, String password){
        User user = userRepository.findById(userId).orElseThrow(InvalidUserException::new);
        if (!passwordEncoder.matches(password, user.encodedPassword())) {
            throw new AuthenticationException();
        }
        Token token = new Token(UUID.randomUUID().toString());
        tokens.put(token, userId);
        return token;
    }

    @Override
    public void register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserExistsException();
        }
        userRepository.save(null, new User(null, username, passwordEncoder.encode(password)));
    }

    @Override
    public void logout(Token token){
        if (tokens.containsKey(token)) {
            tokens.remove(token);
        } else {
            throw new InvalidTokenException();
        }
    }

    @Override
    public User authenticate(Token token){
        if (!tokens.containsKey(token)) {
            throw new InvalidTokenException();
        }
        Optional<User> user = userRepository.findById(tokens.get(token));
        if (user.isEmpty()) {
            throw new InvalidUserException();
        }
        return user.get();
    }
}
