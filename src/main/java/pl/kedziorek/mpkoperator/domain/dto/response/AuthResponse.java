package pl.kedziorek.mpkoperator.domain.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;

    private String refreshToken;

    @Builder.Default
    private String type = "Bearer";

    private String username;

    private List<String> roles;
}
