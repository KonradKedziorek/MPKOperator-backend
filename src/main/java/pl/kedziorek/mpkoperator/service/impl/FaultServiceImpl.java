package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.kedziorek.mpkoperator.config.exception.ResourceNotFoundException;
import pl.kedziorek.mpkoperator.domain.Fault;
import pl.kedziorek.mpkoperator.domain.dto.request.FaultRequest;
import pl.kedziorek.mpkoperator.domain.dto.response.DataResponse;
import pl.kedziorek.mpkoperator.repository.FaultRepository;
import pl.kedziorek.mpkoperator.service.FaultHistoryService;
import pl.kedziorek.mpkoperator.service.FaultService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.kedziorek.mpkoperator.utils.LocalDateConverter.convertToLocalDate;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FaultServiceImpl implements FaultService {
    private final FaultRepository faultRepository;
    private final FaultHistoryService faultHistoryService;

    @Override
    public Fault saveFault(FaultRequest faultRequest) {
        log.info("Saving new fault to the database");
        Fault fault = Fault.map(faultRequest);
        Fault faultResult = faultRepository.save(fault);

        faultHistoryService.saveComplaintInFaultHistory(faultResult, faultResult.getUuid());
        return faultResult;
    }

    @Override
    public Fault findByUuid(UUID uuid) {
        return faultRepository.findByUuid(uuid).orElseThrow(() ->
                new ResourceNotFoundException("Fault not found in database"));
    }

    @Override
    public Fault updateFault(Fault fault, UUID uuid) {
        Fault updatedFault = faultRepository.findByUuid(uuid).orElseThrow(() ->
                new ResourceNotFoundException("Fault not found in the database"));

        log.info("Updating fault with uuid: {} to the database", updatedFault.getUuid());

        updatedFault.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        updatedFault.setModifiedAt(LocalDateTime.now());
        updatedFault.setFaultStatus(fault.getFaultStatus());

        faultHistoryService.saveComplaintInFaultHistory(updatedFault, updatedFault.getUuid());

        return faultRepository.save(updatedFault);
    }

    @Override
    public DataResponse<?> getFaults(Map<String, String> params, int page, int size) {
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
}
