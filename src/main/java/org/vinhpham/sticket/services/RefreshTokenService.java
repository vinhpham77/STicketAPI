package org.vinhpham.sticket.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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

    @Transactional
    public void saveRefreshToken(String deviceId, String refreshToken) {
        redisTemplate.opsForValue().set(deviceId, refreshToken, refreshTokenTtl);
    }

    public Optional<String> get(String deviceId) {
        return Optional.ofNullable((String) redisTemplate.opsForValue().get(deviceId));
    }

    public void deleteRefreshToken(String deviceId) {
        redisTemplate.delete(deviceId);
    }

}
