package com.github.backendpart.web.controller;


import com.github.backendpart.service.UserInfoService;
import com.github.backendpart.web.dto.UserInfoDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserInfoController {
    private UserInfoService userInfoService;

    @GetMapping("/{userCid}/info")
    public ResponseEntity<UserInfoDto> userInfo(@PathVariable Long userCid){
        UserInfoDto userInfoDto = userInfoService.getUserInfoByUserId(userCid);
        return ResponseEntity.ok(userInfoDto);
    }

}
