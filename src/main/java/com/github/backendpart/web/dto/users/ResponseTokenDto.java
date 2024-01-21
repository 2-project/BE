package com.github.backendpart.web.dto.users;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

// 사용자가 로그인을 성공하는 등 인증되어 토큰을 받게 된다면 Body에 accessToken을
// 헤더에 refreshToken을 넣어 보내줄 예정
@Data
@Builder
@AllArgsConstructor
@JsonNaming()
public class ResponseTokenDto {
    private String accessToken;
    private String refreshToken;
}
