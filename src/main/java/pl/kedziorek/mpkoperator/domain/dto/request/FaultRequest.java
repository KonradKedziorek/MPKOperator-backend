package pl.kedziorek.mpkoperator.domain.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class FaultRequest {
    private String uuid;

    @NotBlank(message = "Date of event cannot be blank")
    private String dateOfEvent;

    @NotBlank(message = "Time of event cannot be blank")
    private String timeOfEvent;

    @NotBlank(message = "Place of event cannot be blank")
    private String placeOfEvent;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotBlank(message = "Bus number cannot be blank")
    private String busNumber;
}
