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

    private String registrationNumber;

    private LocalDate firstRegistrationDate;

    private LocalDate insuranceExpiryDate;

    private LocalDate serviceExpiryDate;

    private BusStatus busStatus;
}
