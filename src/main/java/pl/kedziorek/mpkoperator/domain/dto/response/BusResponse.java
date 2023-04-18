package pl.kedziorek.mpkoperator.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import pl.kedziorek.mpkoperator.domain.enums.BusStatus;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class BusResponse {
    private UUID uuid;

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

    private Integer numberOfStandingRoom;

    private LocalDate insuranceExpiryDate;

    private LocalDate serviceExpiryDate;

    private BusStatus busStatus;
}
