package com.github.backendpart.web.controller;


import com.github.backendpart.service.UnregisterService;
import com.github.backendpart.web.dto.UnregisterDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UnregisterController {
    private UnregisterService unregisterService;

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId, @RequestBody UnregisterDto unregisterDto) {
        unregisterService.deleteUser(userId, unregisterDto.getPassword());

        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
}
