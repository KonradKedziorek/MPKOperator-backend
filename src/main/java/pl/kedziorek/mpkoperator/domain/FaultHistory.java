package pl.kedziorek.mpkoperator.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.kedziorek.mpkoperator.domain.dto.response.FaultHistoryResponse;
import pl.kedziorek.mpkoperator.domain.enums.FaultStatus;

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
@Table(name = "fault_history", schema = "public")
public class FaultHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    private LocalDateTime dateOfEvent;

    private LocalDate date;

    @NotBlank
    private String placeOfEvent;

    @NotBlank
    private String description;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Enumerated(EnumType.STRING)
    private FaultStatus faultStatus;

    @OneToOne //To do usuwania kaskadowego więc nie wiadomo czy się przyda
    @JoinColumn(name = "bus_id", referencedColumnName = "id")
    private Bus bus;

    private String createdByOriginalFault;

    private LocalDateTime createdAtOriginalFault;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FaultHistory faultHistory = (FaultHistory) o;
        return id != null && Objects.equals(id, faultHistory.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public static FaultHistory createFaultHistory(Fault fault, UUID uuid) {
        return FaultHistory.builder()
                .uuid(fault.getUuid())
                .dateOfEvent(fault.getDateOfEvent())
                .date(LocalDate.now())
                .placeOfEvent(fault.getPlaceOfEvent())
                .description(fault.getDescription())
                .createdBy(SecurityContextHolder.getContext().getAuthentication().getName())
                .createdAt(LocalDateTime.now())
                .modifiedBy(fault.getModifiedBy())
                .modifiedAt(fault.getModifiedAt())
                .faultStatus(fault.getFaultStatus())
                .bus(fault.getBus())
                .createdAtOriginalFault(fault.getCreatedAt())
                .createdByOriginalFault(fault.getCreatedBy())
                .build();
    }

    public static FaultHistoryResponse mapToFaultHistoryResponse(FaultHistory faultHistory) {
        return FaultHistoryResponse.builder()
                .status(faultHistory.getFaultStatus())
                .modifiedAt(faultHistory.getModifiedAt())
                .modifiedBy(faultHistory.getModifiedBy())
                .build();
    }
}
