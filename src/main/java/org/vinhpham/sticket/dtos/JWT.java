package org.vinhpham.sticket.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JWT {

    String accessToken;
    String refreshToken;

}
