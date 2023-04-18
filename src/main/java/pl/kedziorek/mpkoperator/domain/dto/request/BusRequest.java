package pl.kedziorek.mpkoperator.domain.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class BusRequest {
    private String uuid;

    @NotBlank(message = "Bus number cannot be blank")
    private String busNumber;

    @NotBlank(message = "Mileage cannot be blank")
    private String mileage;

    @NotBlank(message = "Manufacture year cannot be blank")
    private String manufactureYear;

    @NotBlank(message = "Registration number cannot be blank")
    private String registrationNumber;

    @NotBlank(message = "First registration date cannot be blank")
    private String firstRegistrationDate;

    @NotBlank(message = "Brand cannot be blank")
    private String brand;

    @NotBlank(message = "Model cannot be blank")
    private String model;

    @NotBlank(message = "VIN cannot be blank")
    private String VIN;

    @NotBlank(message = "Maximum total mass cannot be blank")
    private String maximumTotalMass;  // maksymalna masa całkowita//

    @NotBlank(message = "Dead weight load cannot be blank")
    private String deadWeightLoad; // masa własna//

    @NotBlank(message = "Engine size cannot be blank")
    private String engineSize;  // pojemność silnika

    @NotBlank(message = "Number of seating cannot be blank")
    private String numberOfSeating; // liczba miejsc siedzących//

    @NotBlank(message = "Number of Standing room cannot be blank")
    private String numberOfStandingRoom;  // liczba miejsc stojących//

    @NotBlank(message = "Insurance expiry date cannot be blank")
    private String insuranceExpiryDate;  // data ważności ubezpieczenia

    @NotBlank(message = "Service expiry date cannot be blank")
    private String serviceExpiryDate; // data ważności przeglądu
}
