package pl.kedziorek.mpkoperator.domain.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class FaultRequest {
    private LocalDateTime dateOfEvent;

    @NotBlank
    private String placeOfEvent;

    @NotBlank
    private String description;
}
