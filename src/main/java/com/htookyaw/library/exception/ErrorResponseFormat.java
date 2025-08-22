package com.htookyaw.library.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.Instant;

@AllArgsConstructor
@Data
public class ErrorResponseFormat {
    private String message;
    private HttpStatus httpStatus;
    private Instant time;
}
