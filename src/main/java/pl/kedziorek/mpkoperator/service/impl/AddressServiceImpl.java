package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.kedziorek.mpkoperator.domain.Address;
import pl.kedziorek.mpkoperator.domain.User;
import pl.kedziorek.mpkoperator.repository.AddressRepository;
import pl.kedziorek.mpkoperator.service.AddressService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    @Override
    public Set<User> getUsersByPesel(Set<String> pesel) {
        return null;
    }

    @Override
    public Address findFirstByCityAndPostcodeAndStreetAndLocalNumberAndHouseNumber(
            String city, String postcode, String street, String localNumber, String houseNumber) {
        return addressRepository.findFirstByCityAndPostcodeAndStreetAndLocalNumberAndHouseNumber(
                city, postcode, street, localNumber, houseNumber).orElseGet(() -> (addressRepository.save(
                Address.builder()
                        .uuid(UUID.randomUUID())
                        .city(city)
                        .postcode(postcode)
                        .street(street)
                        .localNumber(localNumber)
                        .houseNumber(houseNumber)
                        .createdBy(SecurityContextHolder.getContext().getAuthentication().getName())
                        .createdAt(LocalDateTime.now())
                        .build()
        )));
    }
}
