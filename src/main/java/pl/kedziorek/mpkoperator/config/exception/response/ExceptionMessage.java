package pl.kedziorek.mpkoperator.config.exception.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@SuperBuilder
@Data
@AllArgsConstructor
public class ExceptionMessage {
    private LocalDateTime timestamp;

    private String message;

    private HttpStatus status;

    @JsonIgnore
    private Integer customStatus;

    public int getStatus() {
        return customStatus == null ? status.value() : customStatus;
    }

    public String getError() {
        return status == null ? "" : status.getReasonPhrase();
    }
}
