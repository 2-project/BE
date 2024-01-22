package com.github.backendpart.web.controller;


import com.github.backendpart.service.UnregisterService;
import com.github.backendpart.web.dto.UnregisterDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
@AllArgsConstructor
public class UnregisterController {
    private UnregisterService unregisterService;

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId, @RequestBody UnregisterDto unregisterDto) {
        try {
            log.info("DELETE 회원 삭제 요청이 들어왔습니다.");
            unregisterService.deleteUser(userId, unregisterDto.getPassword());
            return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }
    }
}
