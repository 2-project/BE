package com.github.backendpart.web.dto.users;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

// 회원가입 및 수정 등에서 사용
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestUserDto {
    private String userId;
    private String userPwd;
    private String userName;
    private String userPhone;
    private String userAddress;
    private MultipartFile profileImage;
}
