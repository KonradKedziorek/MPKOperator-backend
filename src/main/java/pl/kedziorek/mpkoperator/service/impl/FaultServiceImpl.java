package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.kedziorek.mpkoperator.config.exception.ResourceNotFoundException;
import pl.kedziorek.mpkoperator.domain.Bus;
import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.domain.Fault;
import pl.kedziorek.mpkoperator.domain.dto.request.ComplaintRequest;
import pl.kedziorek.mpkoperator.domain.dto.request.FaultRequest;
import pl.kedziorek.mpkoperator.domain.dto.response.BusResponse;
import pl.kedziorek.mpkoperator.domain.dto.response.DataResponse;
import pl.kedziorek.mpkoperator.domain.enums.FaultStatus;
import pl.kedziorek.mpkoperator.repository.BusRepository;
import pl.kedziorek.mpkoperator.repository.FaultRepository;
import pl.kedziorek.mpkoperator.service.FaultHistoryService;
import pl.kedziorek.mpkoperator.service.FaultService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.kedziorek.mpkoperator.utils.LocalDateConverter.convertToLocalDate;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FaultServiceImpl implements FaultService<Fault> {
    private final FaultRepository faultRepository;
    private final BusRepository busRepository;
    private final FaultHistoryService faultHistoryService;

    @Override
    public Fault findByUuid(UUID uuid) {
        return faultRepository.findByUuid(uuid).orElseThrow(() ->
                new ResourceNotFoundException("Fault not found in database"));
    }

    @Override
    public Fault saveOrUpdateFault(FaultRequest faultRequest) {
        log.info("Saving new fault to the database");
        //if uuid is null should create new object
        if (Objects.equals(faultRequest.getUuid(), "")) {
            return saveFault(faultRequest);
        }//else update existing object
        return editFault(faultRequest);
    }

    @Override
    public Fault saveFault(FaultRequest faultRequest) {
        log.info("Saving new fault to the database");
        Bus bus = busRepository.findByBusNumber(Integer.parseInt(faultRequest.getBusNumber())).orElseThrow(() ->
                new ResourceNotFoundException("Bus with that number does not exist!"));

        Fault fault = Fault.map(faultRequest, bus);
        Fault faultResult = faultRepository.save(fault);

        faultHistoryService.saveComplaintInFaultHistory(faultResult, faultResult.getUuid());
        return faultResult;
    }

    @Override
    public Fault updateFaultStatus(FaultStatus faultStatus, UUID uuid) {
        Fault updatedFault = faultRepository.findByUuid(uuid).orElseThrow(() ->
                new ResourceNotFoundException("Fault not found in the database"));

        log.info("Updating fault with uuid: {} to the database", updatedFault.getUuid());

        updatedFault.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        updatedFault.setModifiedAt(LocalDateTime.now());
        updatedFault.setFaultStatus(faultStatus);

        faultHistoryService.saveComplaintInFaultHistory(updatedFault, updatedFault.getUuid());

        return faultRepository.save(updatedFault);
    }

    private Fault editFault(FaultRequest faultRequest) {
        Fault fault = findByUuid(UUID.fromString(faultRequest.getUuid()));
        var faultRef = changePropertiesValue(faultRequest, fault);
        return faultRepository.save(faultRef);
    }

    @Override
    public DataResponse<Fault> getFaults(Map<String, String> params, int page, int size) {
        Page<Fault> pageFault = faultRepository.findAllParams(
                params.get("placeOfEvent").toUpperCase(),
                params.get("description").toUpperCase(),
                params.get("createdBy").toUpperCase(),
                convertToLocalDate(params.get("date")),
                PageRequest.of(page, size)
        );

        return DataResponse.<Fault>builder()
                .data(pageFault.get().collect(Collectors.toList()))
                .page(pageFault.getTotalPages())
                .size(pageFault.getTotalElements())
                .build();
    }

    private Fault changePropertiesValue(FaultRequest faultRequest, Fault fault) {
        fault.setDateOfEvent(LocalDateTime.of(LocalDate.parse(faultRequest.getDateOfEvent()), LocalTime.now()));
        fault.setPlaceOfEvent(faultRequest.getPlaceOfEvent());
        fault.setDescription(faultRequest.getDescription());
        fault.setModifiedAt(LocalDateTime.now());
        fault.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        fault.setBus(busRepository.findByBusNumber(Integer.parseInt(faultRequest.getBusNumber())).orElseThrow(() ->
                new ResourceNotFoundException("Bus with that number does not exist!")));
        return fault;
    }
}
