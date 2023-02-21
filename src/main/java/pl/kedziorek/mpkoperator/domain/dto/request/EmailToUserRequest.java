package pl.kedziorek.mpkoperator.domain.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class EmailToUserRequest {
    @NotBlank(message = "Subject is mandatory!")
    private String subject;

    @NotBlank(message = "Content is mandatory!")
    private String content;
}
