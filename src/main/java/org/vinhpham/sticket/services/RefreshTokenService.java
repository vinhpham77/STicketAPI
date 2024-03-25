package org.vinhpham.sticket.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.vinhpham.sticket.dtos.RefreshToken;

import java.time.Duration;
import java.util.Optional;

@Service
public class RefreshTokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Duration refreshTokenTtl;

    public RefreshTokenService(RedisTemplate<String, Object> redisTemplate,
                               @Value("${app.security.refresh-token.ttl}") int ttl) {

        this.redisTemplate = redisTemplate;
        this.refreshTokenTtl = Duration.ofDays(ttl);
    }

    public void saveRefreshToken(String deviceId, String refreshToken) {
        RefreshToken token = new RefreshToken(deviceId, refreshToken, refreshTokenTtl);
        redisTemplate.opsForValue().set(deviceId, token, refreshTokenTtl);
    }

    public Optional<RefreshToken> get(String deviceId) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(deviceId)).map(RefreshToken.class::cast);
    }

    public void deleteRefreshToken(String deviceId) {
        redisTemplate.delete(deviceId);
    }

}
