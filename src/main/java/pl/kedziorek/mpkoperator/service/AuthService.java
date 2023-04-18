package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.dto.response.AuthResponse;
import pl.kedziorek.mpkoperator.domain.dto.Credentials;

public interface AuthService {
    AuthResponse authenticate(Credentials credentials) throws IllegalAccessException;
}
