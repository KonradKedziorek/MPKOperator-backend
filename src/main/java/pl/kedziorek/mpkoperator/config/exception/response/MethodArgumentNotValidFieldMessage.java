/**
 * 09 sty 2023 14:18:40
 */
package pl.kedziorek.mpkoperator.config.exception.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author cchojnowski
 */
@Data
@Builder
public class MethodArgumentNotValidFieldMessage {

    private String fieldName;

    private List<String> errorMessages;
}
