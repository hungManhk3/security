package com.example.security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    EXCEPTION(9999, "Uncategorized error", HttpStatus.BAD_REQUEST),
    ERROR_PASS(400, "PassWord was wrong", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED( 401, "Authentication required",HttpStatus.UNAUTHORIZED ),
    FORBIDDEN( 403, "Access is denied",HttpStatus.FORBIDDEN ),
    INVALID_KEY(999, "Invalid message key", HttpStatus.BAD_REQUEST),
    USERNAME_VALID(1001, "Username must be between 3 and 32 characters, not blank and ", HttpStatus.BAD_REQUEST),
    USERNAME_EXITED(1002, "Username has been exited", HttpStatus.BAD_REQUEST),
    NAME_VALID(1003, "Name must be between 2 and 100 char",HttpStatus.CONFLICT),
    NOT_BLANK(1004, "Not BlANK",HttpStatus.BAD_REQUEST),
    USER_NOT_EXITED(1005, "User has been exited",HttpStatus.BAD_REQUEST),


    ;
    private Integer code;
    private String message;
    private HttpStatus status;
}
