package org.vinhpham.sticket.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vinhpham.sticket.dtos.*;
import org.vinhpham.sticket.entities.Device;
import org.vinhpham.sticket.entities.User;
import org.vinhpham.sticket.repositories.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final DeviceService deviceService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public User signup(UserDto request) {
        String username = request.getUsername();
        String email = request.getEmail();

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            throw new ApiException("Tên đăng nhập đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        Optional<User> emailOptional = userRepository.findByEmail(email);

        if (emailOptional.isPresent()) {
            throw new ApiException("Email đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                .username(username)
                .hashPassword(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public JsonWebToken signin(SignInRequest request, String deviceId, String deviceName, String ipAddress) {
        User user;
        String username = request.getUsername();
        String password = request.getPassword();

        try {
            user = (User) authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            throw new ApiException("Tài khoản hoặc mật khẩu không hợp lệ", HttpStatus.UNAUTHORIZED);
        }

        return getJwtResponse(user, deviceId, deviceName, ipAddress);
    }

    public JsonWebToken refreshToken(String refreshToken, String deviceId, String deviceName, String ipAddress) {
        Optional<RefreshToken> token = refreshTokenService.get(deviceId);

        if (token.isEmpty() || !token.get().getRefreshToken().equals(refreshToken)) {
            throw new ApiException("Phiên truy cập đã hết hạn. Vui lòng đăng nhập lại!", HttpStatus.PRECONDITION_FAILED);
        }

        User user = getAuthUser(refreshToken);
        return getJwtResponse(user, deviceId, deviceName, ipAddress);
    }

    private User getAuthUser(String refreshToken) {
        String username = jwtService.extractUserName(refreshToken, true);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("Có lỗi xảy ra. Vui lòng đăng nhập lại!", HttpStatus.NOT_ACCEPTABLE));
    }

    private JsonWebToken getJwtResponse(User user, String deviceId, String deviceName, String ipAddress) {
        Device device = deviceService.findByDeviceId(deviceId);

        if (device == null) {
            Device.builder()
                    .deviceId(deviceId)
                    .deviceName(deviceName)
                    .ipAddress(ipAddress)
                    .build();
        } else {
            device.setDeviceName(deviceName);
            device.setIpAddress(ipAddress);
        }

        deviceService.saveOrUpdate(device);
        String refreshToken = jwtService.generateToken(user, true);
        String accessToken = jwtService.generateToken(user, false);
        refreshTokenService.saveRefreshToken(deviceId, refreshToken);

        return new JsonWebToken(accessToken, refreshToken);
    }

    // TODO: Implement logout
//    @Transactional
//    public void logout(String refreshToken) {
//        var user = getAuthUser(refreshToken);
//        user.setRefreshToken(null);
//        userRepository.save(user);
//    }

}
