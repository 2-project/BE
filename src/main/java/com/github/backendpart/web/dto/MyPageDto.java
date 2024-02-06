package com.github.backendpart.web.dto;


import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageDto {
    private String userName;
    private String userId;
    private Long orderCount;

    @Override
    public String toString() {
        return "MyPageDto{" +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", orderCount='" + orderCount + '\'' +
                '}';
    }
}
