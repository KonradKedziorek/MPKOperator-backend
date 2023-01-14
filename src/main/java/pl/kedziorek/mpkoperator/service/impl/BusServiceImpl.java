package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.kedziorek.mpkoperator.config.exception.ResourceNotFoundException;
import pl.kedziorek.mpkoperator.domain.Bus;
import pl.kedziorek.mpkoperator.domain.dto.request.BusRequest;
import pl.kedziorek.mpkoperator.repository.BusRepository;
import pl.kedziorek.mpkoperator.service.BusService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BusServiceImpl implements BusService {
    private final BusRepository busRepository;

    @Override
    public Bus findByUuid(UUID uuid) {
        return busRepository.findByUuid(uuid).orElseThrow(() ->
                new ResourceNotFoundException("Bus not found in database"));
    }

    @Override
    public Bus saveOrUpdateBus(BusRequest busRequest) {
        log.info("Saving new bus to the database");
        if (Objects.equals(busRequest.getUuid(), "")) {
            return saveBus(busRequest);
        }
        return editBus(busRequest);
    }

    private Bus saveBus(BusRequest busRequest) {
        Bus bus = Bus.map(busRequest);
        return busRepository.save(bus);
    }

    private Bus editBus(BusRequest busRequest) {
        Bus bus = findByUuid(UUID.fromString(busRequest.getUuid()));
        var busRef = changePropertiesValue(busRequest, bus);
        return busRepository.save(busRef);
    }

    // TODO Bus status bedzie sie zmienialo jak bedzie fault wjezdzal na rejon

    private Bus changePropertiesValue(BusRequest busRequest, Bus bus) {
        bus.setBusNumber(Integer.parseInt(busRequest.getBusNumber()));
        bus.setMileage(Long.parseLong(busRequest.getMileage()));
        bus.setManufactureYear(Integer.parseInt(busRequest.getManufactureYear()));
        bus.setRegistrationNumber(busRequest.getRegistrationNumber());
        bus.setFirstRegistrationDate(LocalDate.parse(busRequest.getFirstRegistrationDate()));
        bus.setBrand(busRequest.getBrand());
        bus.setModel(busRequest.getModel());
        bus.setVIN(busRequest.getVIN());
        bus.setMaximumTotalMass(Integer.parseInt(busRequest.getMaximumTotalMass()));
        bus.setDeadWeightLoad(Integer.parseInt(busRequest.getDeadWeightLoad()));
        bus.setEngineSize(Double.parseDouble(busRequest.getEngineSize()));
        bus.setNumberOfSeating(Integer.parseInt(busRequest.getNumberOfSeating()));
        bus.setNumberOfStandingRoom(Integer.parseInt(busRequest.getNumberOfStandingRoom()));
        bus.setInsuranceExpiryDate(LocalDate.parse(busRequest.getInsuranceExpiryDate()));
        bus.setServiceExpiryDate(LocalDate.parse(busRequest.getServiceExpiryDate()));
        bus.setModifiedAt(LocalDateTime.now());
        bus.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        return bus;
    }
}
