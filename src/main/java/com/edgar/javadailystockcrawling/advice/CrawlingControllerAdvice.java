package com.edgar.javadailystockcrawling.advice;

import com.edgar.javadailystockcrawling.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CrawlingControllerAdvice {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity exception(Exception e) {
      log.error("",e);
      return ResponseEntity
              .status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("");
    }

    @ExceptionHandler(value = AuthException.class)
    public ResponseEntity authException(AuthException ae) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build();
    }

}
