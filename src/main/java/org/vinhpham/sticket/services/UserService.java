package org.vinhpham.sticket.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.vinhpham.sticket.dtos.ApiException;
import org.vinhpham.sticket.entities.User;
import org.vinhpham.sticket.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDetailsService userDetailsService() {
        return username -> userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng @" + username));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("Không tìm thấy người dùng @" + username, HttpStatus.NOT_FOUND));
    }

    public Optional<List<User>> getAllUser() {
        return Optional.of(userRepository.findAll());
    }
}
