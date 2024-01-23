package com.github.backendpart.web.dto.users;

import com.github.backendpart.web.entity.enums.Roles;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {
    private String userId;
    private String username;
    private String userPhone;
    private String userAddress;
    private Roles roles;

    @Builder
    public UserDto(String userId, String username, String userPhone, String userAddress, Roles roles) {
      this.userId = userId;
      this.username = username;
      this.userPhone = userPhone;
      this.userAddress = userAddress;
      this.roles = roles;
    }
}
