package org.vinhpham.sticket.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vinhpham.sticket.common.Utils;
import org.vinhpham.sticket.dtos.HandleException;
import org.vinhpham.sticket.dtos.JWT;
import org.vinhpham.sticket.dtos.Login;
import org.vinhpham.sticket.dtos.UserDto;
import org.vinhpham.sticket.entities.Device;
import org.vinhpham.sticket.entities.User;
import org.vinhpham.sticket.repositories.UserRepository;

import java.util.Optional;

import static org.vinhpham.sticket.common.Constants.IS_ACCESS_TOKEN;
import static org.vinhpham.sticket.common.Constants.IS_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final DeviceService deviceService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @Transactional
    public User register(UserDto request) {
        String username = request.getUsername();
        String email = request.getEmail();

        Optional<User> userOptional = userRepository.findById(username);
        String message;

        if (userOptional.isPresent()) {
            message = Utils.getMessage(messageSource, "error.username.exists");
            throw HandleException.bad(message);
        }

        Optional<User> emailOptional = userRepository.findByEmail(email);

        if (emailOptional.isPresent()) {
            message = Utils.getMessage(messageSource, "error.email.exists");
            throw HandleException.bad(message);
        }

        String hashPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .username(username)
                .hashPassword(hashPassword)
                .email(request.getEmail())
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public JWT login(Login request, String deviceId, String deviceName) {
        String username = request.getUsername();
        String password = request.getPassword();
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            String message = Utils.getMessage(messageSource, "error.username.password.invalid");
            throw new HandleException(message, HttpStatus.UNAUTHORIZED);
        }

        User user = (User) authentication.getPrincipal();

        return getJWTs(user, deviceId, deviceName);
    }

    @Transactional
    public JWT refreshToken(String refreshToken, String deviceId, String deviceName) {
        Optional<String> jwt = refreshTokenService.get(deviceId);

        if (jwt.isEmpty() || !refreshToken.equals(jwt.get())) {
            String message = Utils.getMessage(messageSource, "error.jwt.session.expired");
            throw new HandleException(message, HttpStatus.PRECONDITION_FAILED);
        }

        User user = getAuthUser(refreshToken);
        return getJWTs(user, deviceId, deviceName);
    }

    private User getAuthUser(String refreshToken) {
        String username = jwtService.extractUserName(refreshToken, IS_REFRESH_TOKEN);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    String message = Utils.getMessage(messageSource, "error.auth.wrong");
                    return new HandleException(message, HttpStatus.NOT_ACCEPTABLE);
                });
    }

    private JWT getJWTs(User user, String deviceId, String deviceName) {
        Device device = null;

        if (deviceId != null && !deviceId.isBlank()) {
            device = deviceService.findById(deviceId).orElse(null);
        }

        if (device == null) {
            device = Device.builder()
                    .deviceName(deviceName)
                    .build();
        } else {
            device.setDeviceName(deviceName);
        }

        deviceService.saveOrUpdate(device);
        String refreshToken = jwtService.generateToken(user, IS_REFRESH_TOKEN);
        String accessToken = jwtService.generateToken(user, IS_ACCESS_TOKEN);
        refreshTokenService.saveRefreshToken(device.getDeviceId(), refreshToken);

        return new JWT(accessToken, refreshToken);
    }

    // TODO: Implement logout
//    @Transactional
//    public void logout(String refreshToken) {
//        var user = getAuthUser(refreshToken);
//        user.setRefreshToken(null);
//        userRepository.save(user);
//    }

}
