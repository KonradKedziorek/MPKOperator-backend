package pl.kedziorek.mpkoperator.config.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.kedziorek.mpkoperator.config.exception.BadRequestException;
import pl.kedziorek.mpkoperator.config.exception.ResourceNotFoundException;
import pl.kedziorek.mpkoperator.config.exception.response.ExceptionMessage;
import pl.kedziorek.mpkoperator.config.exception.response.MethodArgumentNotValidExceptionResponse;
import pl.kedziorek.mpkoperator.config.exception.response.MethodArgumentNotValidFieldMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handlerException(ResourceNotFoundException resourceNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resourceNotFoundException.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handlerException(BadRequestException badRequestException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestException.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.debug("MethodArgumentNotValidException was thrown - GlobalExceptionHandler", exception);
        Map<String, List<String>> errors = exception.getBindingResult().getFieldErrors().stream().collect(groupingBy(FieldError::getField)).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, s1-> s1.getValue().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList())));

        List<MethodArgumentNotValidFieldMessage> methodArgumentNotValidFieldMessages = errors.entrySet().stream().map(e -> MethodArgumentNotValidFieldMessage.builder()
                        .fieldName(e.getKey())
                        .errorMessages(e.getValue())
                        .build())
                .collect(Collectors.toList());

        final HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        return ResponseEntity.status(status).body(bindToMethodArgumentNotValidExceptionResponse(methodArgumentNotValidFieldMessages,status, "Argument validation fails"));
    }

    @ExceptionHandler(NullPointerException.class)
    public final ResponseEntity<ExceptionMessage> handleNullPointerException(NullPointerException exception) {
        log.debug("NullPointerException was thrown", exception);
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(bindToExceptionMessage(exception.getMessage(), status));
    }

    private MethodArgumentNotValidExceptionResponse bindToMethodArgumentNotValidExceptionResponse(List<MethodArgumentNotValidFieldMessage> errors, HttpStatus status, String message){
        return MethodArgumentNotValidExceptionResponse.builder()
                .errors(errors)
                .status(status)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private ExceptionMessage bindToExceptionMessage(String message, HttpStatus status) {
        return ExceptionMessage.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .status(status)
                .build();
    }
}
