package com.github.backendpart.web.controller;


import com.github.backendpart.service.MyPageService;
import com.github.backendpart.web.dto.MyPageDto;
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
@RequestMapping("/api/mypage")
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/{userCid}")
    public ResponseEntity<MyPageDto> getMyPageInfo(@PathVariable Long userCid) {
        MyPageDto myPageDTO = myPageService.getMyPageInfo(userCid);
        return ResponseEntity.ok(myPageDTO);
    }
}