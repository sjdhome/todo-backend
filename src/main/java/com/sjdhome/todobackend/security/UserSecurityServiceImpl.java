package com.sjdhome.todobackend.security;

import com.sjdhome.todobackend.user.InvalidUserException;
import com.sjdhome.todobackend.user.User;
import com.sjdhome.todobackend.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserSecurityServiceImpl implements UserSecurityService {
    private final UserSecurityRepository securityRepository;
    private final UserRepository userRepository;
    private final List<Token> tokenList;
    private final PasswordEncoder passwordEncoder;

    public UserSecurityServiceImpl(UserSecurityRepository securityRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.securityRepository = securityRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenList = new LinkedList<>();
    }

    @Override
    public Token login(String username, String password) throws AuthenticationException, UserHasNoSecurityMethodsException, InvalidUserException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new InvalidUserException();
        }
        Optional<UserSecurity> us = securityRepository.findById(username);
        if (us.isEmpty()) {
            throw new UserHasNoSecurityMethodsException();
        }
        UserSecurity userSecurity = us.get();
        if (!passwordEncoder.matches(password, userSecurity.encodedPassword())) {
            throw new AuthenticationException();
        }
        Token token = new Token(UUID.randomUUID().toString());
        tokenList.add(token);
        return token;
    }

    @Override
    public void logout(Token token) throws InvalidTokenException {
        if (tokenList.contains(token)) {
            tokenList.remove(token);
        } else {
            throw new InvalidTokenException();
        }
    }

    @Override
    public void register(String username, String password) throws InvalidUserException, PasswordExistsException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new InvalidUserException();
        }
        if (securityRepository.findById(username).isPresent()) {
            throw new PasswordExistsException();
        }
        securityRepository.save(new UserSecurity(username, passwordEncoder.encode(password)));
    }

    @Override
    public void authenticate(Token token) throws InvalidTokenException {
        if (!tokenList.contains(token)) {
            throw new InvalidTokenException();
        }
    }
}
