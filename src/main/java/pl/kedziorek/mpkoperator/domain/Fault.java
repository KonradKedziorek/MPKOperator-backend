package pl.kedziorek.mpkoperator.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.kedziorek.mpkoperator.domain.dto.request.FaultRequest;
import pl.kedziorek.mpkoperator.domain.dto.response.FaultResponse;
import pl.kedziorek.mpkoperator.domain.enums.FaultStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fault", schema = "public")
@Data
@EqualsAndHashCode
public class Fault {
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

    @OneToMany(mappedBy = "fault")
    private List<Comment> comments;

    @OneToOne(cascade = CascadeType.ALL) //To do usuwania kaskadowego więc nie wiadomo czy się przyda
    @JoinColumn(name = "bus_id", referencedColumnName = "id")
    private Bus bus;

    public static Fault map(FaultRequest faultRequest) {
        return Fault.builder()
                .uuid(UUID.randomUUID())
                .dateOfEvent(faultRequest.getDateOfEvent())
                .date(LocalDate.now())
                .placeOfEvent(faultRequest.getPlaceOfEvent())
                .description(faultRequest.getDescription())
                .createdBy(SecurityContextHolder.getContext().getAuthentication().getName())
                .createdAt(LocalDateTime.now())
                .faultStatus(FaultStatus.REPORTED)
                .build();
    }

    public static FaultResponse responseMap(Fault fault) {
        return FaultResponse.builder()
                .uuid(fault.getUuid())
                .dateOfEvent(fault.getDateOfEvent())
                .placeOfEvent(fault.getPlaceOfEvent())
                .description(fault.getDescription())
                .createdBy(fault.getCreatedBy())
                .createdAt(fault.getCreatedAt())
                .modifiedBy(fault.getModifiedBy())
                .modifiedAt(fault.getModifiedAt())
                .faultStatus(fault.getFaultStatus())
                .build();
    }
}
