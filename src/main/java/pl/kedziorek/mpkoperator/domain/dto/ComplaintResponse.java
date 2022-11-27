package pl.kedziorek.mpkoperator.domain.dto;

import lombok.Builder;
import lombok.Data;
import pl.kedziorek.mpkoperator.domain.Complaint;
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

    public static ComplaintResponse map(Complaint complaint) {
        return ComplaintResponse.builder()
                .uuid(complaint.getUuid())
                .dateOfEvent(complaint.getDateOfEvent())
                .placeOfEvent(complaint.getPlaceOfEvent())
                .nameOfNotifier(complaint.getNameOfNotifier())
                .surnameOfNotifier(complaint.getSurnameOfNotifier())
                .peselOfNotifier(complaint.getPeselOfNotifier())
                .complaintStatus(complaint.getComplaintStatus())
                .build();
    }
}
