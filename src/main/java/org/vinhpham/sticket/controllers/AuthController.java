package org.vinhpham.sticket.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vinhpham.sticket.common.Constants;
import org.vinhpham.sticket.dtos.*;
import org.vinhpham.sticket.entities.User;
import org.vinhpham.sticket.services.AuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final MessageSource messageSource;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Login login, HttpServletRequest request) {
        String deviceId = request.getHeader(Constants.KEY_DEVICE_ID);
        String deviceName = request.getHeader(Constants.KEY_DEVICE_NAME);

        JWT jwt = authenticationService.login(login, deviceId, deviceName);

        return Success.ok(jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto) {
        User newUser = authenticationService.register(userDto);

        if (newUser != null) {
            return Success.response(newUser, HttpStatus.CREATED);
        } else {
            return Failure.internal(messageSource, "error.register.failed");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return Failure.internal(messageSource, "error.something.wrong");
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh_token")) {
                // String refreshToken = cookie.getValue();
                // TODO authenticationService.logout(refreshToken);
                return Success.noContent();
            }
        }

        return Failure.internal(messageSource, "error.something.wrong");
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> getRefreshToken(HttpServletRequest request) {
        String deviceId = request.getHeader(Constants.KEY_DEVICE_ID);
        String deviceName = request.getHeader(Constants.KEY_DEVICE_NAME);
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return Failure.internal(messageSource, "error.something.wrong");
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(Constants.KEY_REFRESH_TOKEN)) {
                String refreshToken = cookie.getValue();
                JWT jwt = authenticationService.refreshToken(refreshToken, deviceId, deviceName);
                return Success.ok(jwt);
            }
        }

        return Failure.internal(messageSource, "error.something.wrong");
    }
}
