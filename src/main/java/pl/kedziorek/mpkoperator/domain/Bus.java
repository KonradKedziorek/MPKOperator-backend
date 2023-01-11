package pl.kedziorek.mpkoperator.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import pl.kedziorek.mpkoperator.domain.enums.BusStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bus", schema = "public")
@Data
@EqualsAndHashCode
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    private Integer busNumber;

    private Integer manufactureYear;

    private String registrationNumber;

    private LocalDateTime firstRegistrationDate;

    private String brand;

    private String model;

    private String VIN;

    private Integer maximumTotalMass;  // maksymalna masa całkowita

    private Integer deadWeightLoad; // masa własna

    private Double engineSize;  // pojemność silnika

    private Integer numberOfSeating; // liczba miejsc siedzących

    private Integer numberOfStandingRoom;  // liczba miejsc stojących

    private LocalDateTime insuranceExpiryDate;  // data ważności ubezpieczenia

    private LocalDateTime serviceExpiryDate;  // data ważności przeglądu

    @Enumerated(EnumType.STRING)
    private BusStatus busStatus;

    @OneToMany(mappedBy = "bus")
    private List<User> users;  //lista userow

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
