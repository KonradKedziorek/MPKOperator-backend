package pl.kedziorek.mpkoperator.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import pl.kedziorek.mpkoperator.domain.enums.FaultStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class FaultHistoryResponse {
    private FaultStatus status;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}
