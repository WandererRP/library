package kz.github.rolandp.libraryapiservice.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Roland Pilpani 02.03.2025
 */
public class CustomLibraryException extends RuntimeException {
    private final HttpStatus status;
    private final String errorCode;


    public CustomLibraryException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
