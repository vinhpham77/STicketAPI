package org.vinhpham.sticket.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vinhpham.sticket.dtos.JsonWebToken;
import org.vinhpham.sticket.dtos.SignInRequest;
import org.vinhpham.sticket.dtos.UserDto;
import org.vinhpham.sticket.entities.User;
import org.vinhpham.sticket.services.AuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody SignInRequest signin, HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        String deviceName = request.getHeader("X-Device-Name");
        String deviceId = request.getHeader("X-Device-Id");

        JsonWebToken response = authenticationService.signin(signin, deviceId, deviceName, ipAddress);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserDto signup) {
        User newUser = authenticationService.signup(signup);
        if (newUser != null) {
            return new ResponseEntity<>(newUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Đăng ký không thành công", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Chưa cung cấp refresh token");
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh_token")) {
                String refreshToken = cookie.getValue();
                // TODO authenticationService.logout(refreshToken);
                return ResponseEntity.ok("Đăng xuất thành công!");
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Chưa cung cấp refresh token");
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> getRefreshToken(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        String deviceName = request.getHeader("X-Device-Name");
        String deviceId = request.getHeader("X-Device-Id");
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Chưa cung cấp refresh token");
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh_token")) {
                String refreshToken = cookie.getValue();
                JsonWebToken response = authenticationService.refreshToken(refreshToken, deviceId, deviceName, ipAddress);
                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Chưa cung cấp refresh token");
    }
}
