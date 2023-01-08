package pl.kedziorek.mpkoperator.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import pl.kedziorek.mpkoperator.domain.enums.ComplaintStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ComplaintResponse {
    private UUID uuid;
    private LocalDateTime dateOfEvent;
    private String placeOfEvent;
    private String nameOfNotifier;
    private String surnameOfNotifier;
    private String peselOfNotifier;
    private ComplaintStatus complaintStatus;
    public String contactToNotifier;
    public String description;
}
