package pl.kedziorek.mpkoperator.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import pl.kedziorek.mpkoperator.domain.enums.FaultStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class FaultResponse {
    private UUID uuid;

    private LocalDateTime dateOfEvent;

    private String placeOfEvent;

    private String description;

    private String createdBy;

    private LocalDateTime createdAt;

    private Integer busNumber;

    private FaultStatus faultStatus;
}
