package com.github.backendpart.web.Advice;

import com.amazonaws.services.kms.model.AlreadyExistsException;
import com.github.backendpart.web.dto.common.CommonResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<CommonResponseDto> handleNotFoundException(NoSuchElementException nse) {
        log.error("Client 요청 이후 DB 검색 중 에러로 다음처럼 출력합니다." + nse.getMessage());
        return ResponseEntity.ok().body(CommonResponseDto.builder()
                .code(404).success(false).message(nse.getMessage()).build());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<CommonResponseDto> handleAlreadyExistException(AlreadyExistsException aee) {
        log.error("Client 요청 이후 DB 검색 중 에러로 다음처럼 출력합니다." + aee.getMessage());
        return ResponseEntity.ok().body(CommonResponseDto.builder()
                .code(409).success(false).message(aee.getMessage()).build());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<CommonResponseDto> handleOutOfStockException(OutOfStockException ose) {
        log.error("Client 요청 이후 DB 검색 중 에러로 다음처럼 출력합니다." + ose.getMessage());
        return ResponseEntity.ok().body(CommonResponseDto.builder()
                .code(409).success(false).message(ose.getMessage()).build());
    }





}
