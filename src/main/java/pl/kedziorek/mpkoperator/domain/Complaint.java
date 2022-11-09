package pl.kedziorek.mpkoperator.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kedziorek.mpkoperator.domain.enums.ComplaintStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "complaint", schema = "public")
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private LocalDateTime dateOfEvent;

    @NotBlank
    private String placeOfEvent;

    @NotBlank
    private String description;

    @Enumerated(EnumType.STRING)
    @NotBlank
    private ComplaintStatus complaintStatus;
}
