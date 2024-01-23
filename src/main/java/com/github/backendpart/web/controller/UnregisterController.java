package com.github.backendpart.web.controller;


import com.github.backendpart.service.UnregisterService;
import com.github.backendpart.web.dto.UnregisterDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "회원탈퇴 API", description = "회원탈퇴 API입니다.")
public class UnregisterController {
    private UnregisterService unregisterService;

    @Operation(summary = "회원탈퇴 요청", description = "비밀번호 입력시 회원탈퇴한다.")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId, @RequestBody UnregisterDto unregisterDto) {
        unregisterService.deleteUser(userId, unregisterDto.getPassword());

        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
}
