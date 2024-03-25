package org.vinhpham.sticket.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vinhpham.sticket.entities.User;
import org.vinhpham.sticket.services.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    final private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        Optional<List<User>> userListOptional = userService.getAllUser();

        if (userListOptional.isPresent()) {
            List<User> userList = userListOptional.get();
            return new ResponseEntity<>(userList, HttpStatus.OK);
        }

        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
