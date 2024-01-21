package com.github.backendpart.web.Advice;

import com.amazonaws.services.kms.model.AlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNotFoundException(NoSuchElementException nse) {
        log.error("Client 요청 이후 DB 검색 중 에러로 다음처럼 출력합니다." + nse.getMessage());
        return nse.getMessage();
    }
}
