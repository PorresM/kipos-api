package com.kipos.kiposapi.services.auth;

import com.kipos.kiposapi.persistence.entity.User;
import com.kipos.kiposapi.services.user.UserService;
import com.kipos.kiposapi.utils.PasswordUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service that manages /users calls
 */
@Component
public class AuthService {

    private final UserService userService;

    private final TokenAuthenticationService tokenAuthenticationService;

    public AuthService(UserService userService, TokenAuthenticationService tokenAuthenticationService) {
        this.userService = userService;
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    public Optional<String> getToken(String email, String password) {
        return userService.getUserByEmail(email).flatMap(user -> {
            if (PasswordUtils.PASSWORD_ENCODER.matches(password, user.getPassword())) {
                return Optional.of(tokenAuthenticationService.createNewToken(user.getId()));
            }
            return Optional.empty();
        });
    }

    public User getAuthUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }

    public Optional<User> getUser() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return Optional.of((User) SecurityContextHolder.getContext().getAuthentication().getDetails());
    }
}
