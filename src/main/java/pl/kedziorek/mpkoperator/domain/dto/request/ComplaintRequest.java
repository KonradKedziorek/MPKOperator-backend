package pl.kedziorek.mpkoperator.domain.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class ComplaintRequest {

    @NotBlank(message = "Date of event cannot be blank")
    private String dateOfEvent;

    @NotBlank(message = "Time of event cannot be blank")
    private String timeOfEvent;

    @NotBlank(message = "Place of event cannot be blank")
    private String placeOfEvent;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotBlank(message = "Name of notifier cannot be blank")
    private String nameOfNotifier;

    @NotBlank(message = "Surname of notifier cannot be blank")
    private String surnameOfNotifier;

    @NotBlank(message = "Pesel of notifier cannot be blank")
    private String peselOfNotifier;

    @NotBlank(message = "Contact to notifier cannot be blank")
    private String contactToNotifier;

    private String uuid;
}
