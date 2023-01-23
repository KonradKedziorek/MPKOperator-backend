package pl.kedziorek.mpkoperator.domain.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponse {
    private String city;

    private String postcode;

    private String street;

    private String localNumber;

    private String houseNumber;
}
