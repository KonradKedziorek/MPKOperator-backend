package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Address;
import pl.kedziorek.mpkoperator.domain.User;

import java.util.Set;

public interface AddressService {
    Set<User> getUsersByPesel(Set<String> pesel);
    Address findFirstByCityAndPostcodeAndStreetAndLocalNumberAndHouseNumber(
            String city, String postcode, String street, String localNumber, String houseNumber
    );
}
