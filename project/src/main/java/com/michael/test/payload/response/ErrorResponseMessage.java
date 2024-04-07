package com.michael.test.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class ErrorResponseMessage {
    private Date timestamp;
    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String message;

    public ErrorResponseMessage(int httpStatusCode,
                                HttpStatus httpStatus,
                                String message) {
        timestamp = new Date();
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
