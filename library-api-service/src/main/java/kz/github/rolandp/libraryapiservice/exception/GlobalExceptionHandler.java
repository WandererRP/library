package kz.github.rolandp.libraryapiservice.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Roland Pilpani 02.03.2025
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    private final ObjectMapper objectMapper;

    @ExceptionHandler(Exception.class)
    public void handleException(HttpServletResponse response, HttpServletRequest request, Exception e) {
        HttpStatus status = determineHttpStatus(e);
        String errorMessage = determineErrorMessage(e);
        String errorCode = determineErrorCode(e);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setStatus(status.value());

        log.error("ErrorHandlerController Exception handled: URI: {}, Code: {}", request.getRequestURI(), errorCode, e);

        try {
            response.getWriter().write(objectMapper.writeValueAsString(
                    ResponseStatusExceptionBody.builder()
                            .status(status.value())
                            .error(status.getReasonPhrase())
                            .path(request.getRequestURI())
                            .message(errorMessage)
                            .code(errorCode)
                            .build()
            ));
        } catch (IOException ioException) {
            log.error("Failed to write error response: {}", ioException.getMessage(), ioException);
        }
    }

    private HttpStatus determineHttpStatus(Exception e) {
        if (e instanceof ResponseStatusException ex) {
            return HttpStatus.valueOf(ex.getStatusCode().value());
        } else if (e instanceof MethodArgumentNotValidException ||
                e instanceof MissingServletRequestParameterException ||
                e instanceof org.springframework.web.method.annotation.MethodArgumentTypeMismatchException) {
            return HttpStatus.BAD_REQUEST;
        } else if (e instanceof NoResourceFoundException ex) {
            return HttpStatus.valueOf(ex.getStatusCode().value());
        } else if (e instanceof HttpRequestMethodNotSupportedException ex) {
            return HttpStatus.valueOf(ex.getStatusCode().value());
        } else if (e instanceof CustomLibraryException customLibraryException) {
            return customLibraryException.getStatus();
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private String determineErrorMessage(Exception e) {
        return switch (e) {
            case MethodArgumentNotValidException validationException -> validationException.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .findFirst()
                    .map(FieldError::getDefaultMessage)
                    .orElse("Validation error");
            case MissingServletRequestParameterException missingParamException ->
                    "Missing parameter: " + missingParamException.getParameterName();
            case NoResourceFoundException noResourceFoundException -> noResourceFoundException.getMessage();
            case ResponseStatusException responseStatusException -> responseStatusException.getReason();
            case org.springframework.web.method.annotation.MethodArgumentTypeMismatchException methodArgumentTypeMismatchException ->
                    methodArgumentTypeMismatchException.getMessage();
            case HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException ->
                    httpRequestMethodNotSupportedException.getMessage();
            case CustomLibraryException customLibraryException -> customLibraryException.getMessage();
            case null, default -> "An unexpected error occurred.";
        };
    }


    private String determineErrorCode(Exception e) {
        return switch (e) {
            case MethodArgumentNotValidException ignored -> "VALIDATION_ERROR";
            case MissingServletRequestParameterException ignored -> "MISSING_PARAMETER";
            case NoResourceFoundException ignored -> "RESOURCE_NOT_FOUND";
            case ResponseStatusException ex -> "HTTP_" + ex.getStatusCode().value();
            case HttpRequestMethodNotSupportedException ignored -> "METHOD_NOT_ALLOWED";
            case org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ignored ->
                    "TYPE_MISMATCH";
            case CustomLibraryException customLibraryException -> customLibraryException.getErrorCode();
            case null, default -> "INTERNAL_SERVER_ERROR";
        };
    }
}
