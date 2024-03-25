package org.vinhpham.sticket.dtos;

import lombok.Data;

import java.time.Duration;
import java.time.Instant;

@Data
public class RefreshToken {

    private String deviceId;
    private String refreshToken;
    private Instant expiredAt;
    public RefreshToken(String deviceId, String refreshToken, Duration plus) {
        this.deviceId = deviceId;
        this.refreshToken = refreshToken;
        this.expiredAt = Instant.now().plus(plus);
    }
}
