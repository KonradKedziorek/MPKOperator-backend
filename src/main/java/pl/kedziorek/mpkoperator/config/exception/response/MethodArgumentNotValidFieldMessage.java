package pl.kedziorek.mpkoperator.config.exception.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MethodArgumentNotValidFieldMessage {

    private String fieldName;

    private List<String> errorMessages;
}
