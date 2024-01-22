package com.github.backendpart.web.dto.users;

import com.github.backendpart.web.entity.users.UserEntity;
import lombok.*;

@Getter
@AllArgsConstructor
public class ResponseUserDto {
    private String userId;

    public static ResponseUserDto responseUserDto(UserEntity userEntity){
        return new ResponseUserDto(userEntity.getUserId());
    }
}
