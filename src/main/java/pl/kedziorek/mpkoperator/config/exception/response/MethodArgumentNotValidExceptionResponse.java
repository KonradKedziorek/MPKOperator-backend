/**
 * 09 sty 2023 14:23:40
 */
package pl.kedziorek.mpkoperator.config.exception.response;

import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author cchojnowski
 */
@SuperBuilder
public class MethodArgumentNotValidExceptionResponse extends ExceptionMessage {
    public List<MethodArgumentNotValidFieldMessage> errors;
}
