package com.aburakkontas.manga_cdn.api;

import com.aburakkontas.manga_cdn.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_cdn.domain.primitives.Result;
import org.axonframework.queryhandling.QueryExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(QueryExecutionException.class)
    public ResponseEntity<Result<String>> handleQueryExecutionException(QueryExecutionException ex) {
        var details = ex.getDetails().orElseGet(ex::getMessage);
        if(details instanceof Integer) {
            return new ResponseEntity<>(Result.failure(ex.getMessage(), (Integer) details), HttpStatusCode.valueOf((Integer) details));
        }
        return new ResponseEntity<>(Result.failure((String) details, HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ExceptionWithErrorCode.class)
    public ResponseEntity<Result<String>> handleExceptionWithErrorCode(ExceptionWithErrorCode ex) {
        return new ResponseEntity<>(Result.failure(ex.getMessage(), ex.getCode()), HttpStatusCode.valueOf(ex.getCode()));
    }

}
