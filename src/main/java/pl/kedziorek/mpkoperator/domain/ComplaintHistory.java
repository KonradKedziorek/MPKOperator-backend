package pl.kedziorek.mpkoperator.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.kedziorek.mpkoperator.domain.enums.ComplaintStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "complaint_history", schema = "public")
public class ComplaintHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    private LocalDateTime dateOfEvent;

    @NotBlank
    private String placeOfEvent;

    @NotBlank
    private String description;

    @NotBlank
    private String nameOfNotifier;

    @NotBlank
    private String surnameOfNotifier;

    @NotBlank
    private String peselOfNotifier;

    @NotBlank
    private String contactToNotifier;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Enumerated(EnumType.STRING)
    private ComplaintStatus complaintStatus;

    private LocalDateTime createdAtOriginalComplaint;

    private String createdByOriginalComplaint;

    private LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ComplaintHistory complaintHistory = (ComplaintHistory) o;
        return id != null && Objects.equals(id, complaintHistory.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public static ComplaintHistory createComplaintHistory(Complaint complaint, UUID uuid) {
        return ComplaintHistory.builder()
                .uuid(complaint.getUuid())
                .dateOfEvent(complaint.getDateOfEvent())
                .placeOfEvent(complaint.getPlaceOfEvent())
                .nameOfNotifier(complaint.getNameOfNotifier())
                .surnameOfNotifier(complaint.getSurnameOfNotifier())
                .peselOfNotifier(complaint.getPeselOfNotifier())
                .contactToNotifier(complaint.getContactToNotifier())
                .createdBy(SecurityContextHolder.getContext().getAuthentication().getName())
                .createdAtOriginalComplaint(complaint.getCreatedAt())
                .createdByOriginalComplaint(complaint.getCreatedBy())
                .createdAt(LocalDateTime.now())
                .modifiedBy(complaint.getModifiedBy())
                .modifiedAt(complaint.getModifiedAt())
                .complaintStatus(complaint.getComplaintStatus())
                .description(complaint.getDescription())
                .date(LocalDate.now())
                .build();
    }
}
