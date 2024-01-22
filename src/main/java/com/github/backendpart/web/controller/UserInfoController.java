package com.github.backendpart.web.controller;


import com.github.backendpart.service.UserInfoService;
import com.github.backendpart.web.dto.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api")
public class UserInfoController {
    private UserInfoService userInfoService;

    @GetMapping("/{userCid}/info")
    public ResponseEntity<UserInfoDto> userInfo(@PathVariable Long userCid){
        log.info("GET 회원 정보 조회 요청이 들어왔습니다.");
        UserInfoDto userInfoDto = userInfoService.getUserInfoByUserId(userCid);
        log.info("회원 정보 조회 결과: " + userInfoDto);
        return ResponseEntity.ok(userInfoDto);
    }

}
