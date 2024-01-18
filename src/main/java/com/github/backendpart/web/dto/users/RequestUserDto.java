package com.github.backendpart.web.dto.users;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

// 회원가입 및 수정 등에서 사용

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestUserDto {
    private String userId;
    private String userPwd;
    private String userName;
    private String userPhone;
    private String userAddress;
    private MultipartFile profileImage;
}
