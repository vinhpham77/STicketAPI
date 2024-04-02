package org.vinhpham.sticket.controllers;

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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody String refreshToken, @RequestParam(required = false, name = "apply") String apply, HttpServletRequest request) {
        String deviceId = request.getHeader(Constants.KEY_DEVICE_ID);
        authenticationService.logout(deviceId, apply, refreshToken);
        return Success.noContent();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> getRefreshToken(@RequestBody String refreshToken, HttpServletRequest request) {
        String deviceId = request.getHeader(Constants.KEY_DEVICE_ID);
        JWT jwt = authenticationService.refreshToken(refreshToken, deviceId);
        return Success.ok(jwt);
    }
}
