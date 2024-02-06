package com.github.backendpart.web.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnregisterDto {
    private String password;

    @Override
    public String toString() {
        return "OrderHistoryDto{" +
                ", password='" + password + '\'' +
                '}';
    }
}
