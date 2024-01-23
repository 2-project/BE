package com.github.backendpart.web.controller;


import com.github.backendpart.service.MyPageService;
import com.github.backendpart.web.dto.MyPageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "마이페이지 API", description = "마이페이지 조회 API입니다.")
public class MyPageController {
    private final MyPageService myPageService;

    @Operation(summary = "마이페이지 조회", description = "유저의 마이페이지를 조회한다.")
    @GetMapping("/{userCid}")
    public ResponseEntity<MyPageDto> getMyPageInfo(@PathVariable Long userCid) {
        MyPageDto myPageDTO = myPageService.getMyPageInfo(userCid);
        return ResponseEntity.ok(myPageDTO);
    }
}
