package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.kedziorek.mpkoperator.config.exception.ResourceNotFoundException;
import pl.kedziorek.mpkoperator.domain.Bus;
import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.domain.dto.request.BusRequest;
import pl.kedziorek.mpkoperator.domain.dto.response.DataResponse;
import pl.kedziorek.mpkoperator.domain.enums.BusStatus;
import pl.kedziorek.mpkoperator.domain.enums.ComplaintStatus;
import pl.kedziorek.mpkoperator.repository.BusRepository;
import pl.kedziorek.mpkoperator.service.BusService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.kedziorek.mpkoperator.utils.IntegerConverter.convertToInteger;
import static pl.kedziorek.mpkoperator.utils.LocalDateConverter.convertToLocalDate;
import static pl.kedziorek.mpkoperator.utils.LongConverter.convertToLong;

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

    @Override
    public Bus updateBusStatus(BusStatus busStatus, UUID uuid) {
        Bus updatedBus = busRepository.findByUuid(uuid).orElseThrow(() ->
                new ResourceNotFoundException("Bus not found in the database"));

        log.info("Updating bus with uuid: {} to the database", updatedBus.getUuid());

        updatedBus.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        updatedBus.setModifiedAt(LocalDateTime.now());
        updatedBus.setBusStatus(busStatus);

        return busRepository.save(updatedBus);
    }

    // TODO Bus status bedzie sie zmienialo jak bedzie fault wjezdzal na rejon

    @Override
    public DataResponse<Bus> getBuses(Map<String, String> params, int page, int size) {
        Page<Bus> pageBus = busRepository.findAllParams(
                convertToInteger(params.get("busNumber")),  //  == null ? "" : params.get("busNumber")
                convertToLong(params.get("mileage")),
                params.get("registrationNumber") == null ? "" : params.get("registrationNumber"),
                convertToLocalDate(params.get("firstRegistrationDate")),
                convertToLocalDate(params.get("insuranceExpiryDate")),
                convertToLocalDate(params.get("serviceExpiryDate")),
                params.get("busStatus") != null ? BusStatus.valueOf(params.get("busStatus")) : null,
                PageRequest.of(page, size)
                );

        return DataResponse.<Bus>builder()
                .data(pageBus.get().collect(Collectors.toList()))
                .page(pageBus.getTotalPages())
                .size(pageBus.getTotalElements())
                .build();
    }

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
