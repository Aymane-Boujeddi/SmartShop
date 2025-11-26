package com.smartshop.exception;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionResponse {


    private String message;

    private String path;

    private LocalDateTime dateException;

    private HttpStatus httpStatus;

    private int httpCode;
}
