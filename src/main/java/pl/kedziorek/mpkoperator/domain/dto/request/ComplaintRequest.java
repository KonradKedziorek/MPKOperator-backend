package pl.kedziorek.mpkoperator.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pl.kedziorek.mpkoperator.domain.enums.ComplaintStatus;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class ComplaintRequest {
    private LocalDateTime dateOfEvent;

    @NotBlank
    private String placeOfEvent;

    @NotBlank
    private String description;

    @NotBlank
    private String nameOfNotifier;

    @NotBlank
    private String surnameOfNotifier;

    @NotBlank
    private String peselOfNotifier;

    @NotBlank
    private String contactToNotifier;

    private String uuid;
}
