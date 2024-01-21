package com.github.backendpart.web.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

//access Token과 Refresh Token을 담은 정보
@Data
@Builder
@AllArgsConstructor
public class TokenDto {
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private Duration duration;
}
