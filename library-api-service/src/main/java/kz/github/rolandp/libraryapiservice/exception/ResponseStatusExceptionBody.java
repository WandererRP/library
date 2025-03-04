package kz.github.rolandp.libraryapiservice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseStatusExceptionBody {
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    private Integer status;
    private String error;
    @Builder.Default
    private String exception = "ResponseStatusExceptionBody";
    private String path;
    private String code;
    private String message;
}