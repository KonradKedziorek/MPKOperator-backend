/**
 * 08 sty 2023 10:28:51
 */
package pl.kedziorek.mpkoperator.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import pl.kedziorek.mpkoperator.domain.dto.response.CommentResponse;
import pl.kedziorek.mpkoperator.domain.enums.ComplaintStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author cchojnowski
 */
@Data
@Builder
public class ComplaintDetailsResponse {
    private LocalDateTime dateOfEvent;

    private String placeOfEvent;

    private String description;

    private String nameOfNotifier;

    private String surnameOfNotifier;

    private String peselOfNotifier;

    private String contactToNotifier;

    private String createdBy;

    private LocalDateTime createdAt;

    private String modifiedBy;

    private LocalDateTime modifiedAt;

    private ComplaintStatus complaintStatus;

    private List<CommentResponse> comments;

    private LocalDate date;

    private UUID uuid;
}
