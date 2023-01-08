package pl.kedziorek.mpkoperator.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.kedziorek.mpkoperator.domain.dto.request.ComplaintRequest;
import pl.kedziorek.mpkoperator.domain.dto.response.CommentResponse;
import pl.kedziorek.mpkoperator.domain.dto.response.ComplaintDetailsResponse;
import pl.kedziorek.mpkoperator.domain.dto.response.ComplaintResponse;
import pl.kedziorek.mpkoperator.domain.enums.ComplaintStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "complaint", schema = "public")
@Data
@EqualsAndHashCode
public class Complaint {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "complaint")
    private List<Comment> comments;

    private LocalDate date;

    public static Complaint map(ComplaintRequest complaintRequest) {
        return Complaint.builder()
                .uuid(Objects.equals(complaintRequest.getUuid(), "") ? UUID.randomUUID() : UUID.fromString(complaintRequest.getUuid()))
                .dateOfEvent(LocalDateTime.now())
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

    public static ComplaintResponse map2(Complaint complaint) {
        return ComplaintResponse.builder()
                .uuid(complaint.getUuid())
                .dateOfEvent(complaint.getDateOfEvent())
                .placeOfEvent(complaint.getPlaceOfEvent())
                .nameOfNotifier(complaint.getNameOfNotifier())
                .surnameOfNotifier(complaint.getSurnameOfNotifier())
                .peselOfNotifier(complaint.getPeselOfNotifier())
                .complaintStatus(complaint.getComplaintStatus())
                .description(complaint.getDescription())
                .contactToNotifier(complaint.getContactToNotifier())
                .build();
    }

    public static ComplaintDetailsResponse mapToComplaintDetailsResponse(Complaint complaint){
        List<CommentResponse> commentResponseList = complaint.getComments().stream().map(Comment::mapToCommentResponse).collect(Collectors.toList());
        return ComplaintDetailsResponse.builder()
                .uuid(complaint.getUuid())
                .dateOfEvent(complaint.getDateOfEvent())
                .placeOfEvent(complaint.getPlaceOfEvent())
                .description(complaint.getDescription())
                .nameOfNotifier(complaint.getNameOfNotifier())
                .surnameOfNotifier(complaint.getSurnameOfNotifier())
                .peselOfNotifier(complaint.getPeselOfNotifier())
                .contactToNotifier(complaint.getContactToNotifier())
                .createdBy(complaint.getCreatedBy())
                .createdAt(complaint.getCreatedAt())
                .modifiedBy(complaint.getModifiedBy())
                .modifiedAt(complaint.getModifiedAt())
                .complaintStatus(complaint.getComplaintStatus())
                .date(complaint.getDate())
                .comments(commentResponseList)
                .build();
    }
}
