package pl.kedziorek.mpkoperator.domain.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserBusResponse {
    private String name;
    private String surname;
    private String phoneNumber;
}
