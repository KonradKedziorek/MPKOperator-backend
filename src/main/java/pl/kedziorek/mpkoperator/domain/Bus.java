package pl.kedziorek.mpkoperator.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.kedziorek.mpkoperator.domain.dto.request.BusRequest;
import pl.kedziorek.mpkoperator.domain.dto.response.BusDetailsResponse;
import pl.kedziorek.mpkoperator.domain.dto.response.BusResponse;
import pl.kedziorek.mpkoperator.domain.dto.response.CommentResponse;
import pl.kedziorek.mpkoperator.domain.dto.response.UserBusResponse;
import pl.kedziorek.mpkoperator.domain.enums.BusStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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
    
     //TODO Unique doddac
    private Integer busNumber;

    private Long mileage;

    private Integer manufactureYear;

    private String registrationNumber;

    private LocalDate firstRegistrationDate;

    private String brand;

    private String model;

    private String VIN;

    private Integer maximumTotalMass;  // maksymalna masa całkowita

    private Integer deadWeightLoad; // masa własna

    private Double engineSize;  // pojemność silnika

    private Integer numberOfSeating; // liczba miejsc siedzących

    private Integer numberOfStandingRoom;  // liczba miejsc stojących

    private LocalDate insuranceExpiryDate;  // data ważności ubezpieczenia

    private LocalDate serviceExpiryDate;  // data ważności przeglądu

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

    public static Bus map(BusRequest busRequest) {
        return Bus.builder()
                .uuid(Objects.equals(busRequest.getUuid(), "") ? UUID.randomUUID() : UUID.fromString(busRequest.getUuid()))
                .busNumber(Integer.parseInt(busRequest.getBusNumber()))
                .mileage(Long.parseLong(busRequest.getMileage()))
                .manufactureYear(Integer.parseInt(busRequest.getManufactureYear()))
                .registrationNumber(busRequest.getRegistrationNumber())
                .firstRegistrationDate(LocalDate.parse(busRequest.getFirstRegistrationDate()))
                .brand(busRequest.getBrand())
                .model(busRequest.getModel())
                .VIN(busRequest.getVIN())
                .maximumTotalMass(Integer.parseInt(busRequest.getMaximumTotalMass()))
                .deadWeightLoad(Integer.parseInt(busRequest.getDeadWeightLoad()))
                .engineSize(Double.parseDouble(busRequest.getEngineSize()))
                .numberOfSeating(Integer.parseInt(busRequest.getNumberOfSeating()))
                .numberOfStandingRoom(Integer.parseInt(busRequest.getNumberOfStandingRoom()))
                .insuranceExpiryDate(LocalDate.parse(busRequest.getInsuranceExpiryDate()))
                .serviceExpiryDate(LocalDate.parse(busRequest.getServiceExpiryDate()))
                .busStatus(BusStatus.NOT_READY_TO_DRIVE)
                .createdBy(SecurityContextHolder.getContext().getAuthentication().getName())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static BusResponse mapBusToBusResponse(Bus bus) {
        return BusResponse.builder()
                .uuid(bus.getUuid())
                .busNumber(bus.getBusNumber())
                .mileage(bus.getMileage())
                .registrationNumber(bus.getRegistrationNumber())
                .firstRegistrationDate(bus.getFirstRegistrationDate())
                .insuranceExpiryDate(bus.getInsuranceExpiryDate())
                .serviceExpiryDate(bus.getServiceExpiryDate())
                .busStatus(bus.getBusStatus())
                .build();
    }

    public static BusDetailsResponse mapToBusDetailsResponse(Bus bus) {
        List<UserBusResponse> usersResponseList = bus.getUsers().stream().map(User::mapToUserResponse).collect(Collectors.toList());
        return BusDetailsResponse.builder()
                .uuid(bus.getUuid())
                .busNumber(bus.getBusNumber())
                .mileage(bus.getMileage())
                .manufactureYear(bus.getManufactureYear())
                .registrationNumber(bus.getRegistrationNumber())
                .firstRegistrationDate(bus.getFirstRegistrationDate())
                .brand(bus.getBrand())
                .model(bus.getModel())
                .VIN(bus.getVIN())
                .maximumTotalMass(bus.getMaximumTotalMass())
                .deadWeightLoad(bus.getDeadWeightLoad())
                .engineSize(bus.getEngineSize())
                .numberOfSeating(bus.getNumberOfSeating())
                .numberOfStandingRoom(bus.getNumberOfStandingRoom())
                .insuranceExpiryDate(bus.getInsuranceExpiryDate())
                .serviceExpiryDate(bus.getServiceExpiryDate())
                .busStatus(bus.getBusStatus())
                .users(usersResponseList)
                .createdBy(bus.getCreatedBy())
                .createdAt(bus.getCreatedAt())
                .modifiedBy(bus.getModifiedBy())
                .modifiedAt(bus.getModifiedAt())
                .build();
    }
}
