package pl.kedziorek.mpkoperator.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.kedziorek.mpkoperator.domain.dto.ComplaintRequest;
import pl.kedziorek.mpkoperator.domain.enums.ComplaintStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "complaint", schema = "public")
public class Complaint {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    //TODO Rozdzielic date i godzine bo pozniej chyba byl problem przy paginacji i filtrowaniu

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

    @OneToMany(mappedBy = "complaint")
    private Set<Comment> comments;

    private LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Complaint complaint = (Complaint) o;
        return id != null && Objects.equals(id, complaint.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public static Complaint map(ComplaintRequest complaintRequest){
        return Complaint.builder()
                .uuid(UUID.randomUUID())
                .dateOfEvent(complaintRequest.getDateOfEvent())
                .placeOfEvent(complaintRequest.getPlaceOfEvent())
                .nameOfNotifier(complaintRequest.getNameOfNotifier())
                .surnameOfNotifier(complaintRequest.getSurnameOfNotifier())
                .peselOfNotifier(complaintRequest.getPeselOfNotifier())
                .contactToNotifier(complaintRequest.getContactToNotifier())
                .description(complaintRequest.getDescription())
                .complaintStatus(ComplaintStatus.RECEIVED)
                .createdBy(SecurityContextHolder.getContext().getAuthentication().getName())
                .createdAt(LocalDateTime.now())
                .date(LocalDate.now())
                .build();
    }
}
