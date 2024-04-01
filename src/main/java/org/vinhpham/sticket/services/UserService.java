package org.vinhpham.sticket.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.vinhpham.sticket.common.Utils;
import org.vinhpham.sticket.dtos.HandleException;
import org.vinhpham.sticket.entities.User;
import org.vinhpham.sticket.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MessageSource messageSource;

    public UserDetailsService userDetailsService() {
        return username -> userRepository
                .findByUsername(username)
                .orElseThrow(() -> {
                    var message = Utils.getMessage(messageSource, "error.username.not.found", username);
                    return new HandleException(message, HttpStatus.NOT_FOUND);
                });
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<List<User>> getAllUser() {
        return Optional.of(userRepository.findAll());
    }
}
