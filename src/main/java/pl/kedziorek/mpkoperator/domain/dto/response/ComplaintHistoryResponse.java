package pl.kedziorek.mpkoperator.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import pl.kedziorek.mpkoperator.domain.enums.ComplaintStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ComplaintHistoryResponse {
    private ComplaintStatus status;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}
