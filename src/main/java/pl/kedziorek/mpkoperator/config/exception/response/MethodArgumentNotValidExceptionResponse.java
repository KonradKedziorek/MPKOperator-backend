package pl.kedziorek.mpkoperator.config.exception.response;

import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class MethodArgumentNotValidExceptionResponse extends ExceptionMessage {
    public List<MethodArgumentNotValidFieldMessage> errors;
}
