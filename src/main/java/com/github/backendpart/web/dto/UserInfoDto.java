package com.github.backendpart.web.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserInfoDto {

    private Long userCid;
    private String userId;
    private String userPwd;
    private String userName;
    private String userPhone;
    private String userAddress;
}


