package pl.kedziorek.mpkoperator.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import pl.kedziorek.mpkoperator.domain.Comment;
import pl.kedziorek.mpkoperator.domain.enums.FaultStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class FaultDetailsResponse {
    private UUID uuid;

    private LocalDateTime dateOfEvent;

    private LocalDate date;

    private String placeOfEvent;

    private String description;

    private String createdBy;

    private LocalDateTime createdAt;

    private String modifiedBy;

    private LocalDateTime modifiedAt;

    private FaultStatus faultStatus;

    private List<CommentResponse> comments;

    private Integer busNumber;
}
