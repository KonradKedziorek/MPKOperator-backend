package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kedziorek.mpkoperator.domain.Fault;
import pl.kedziorek.mpkoperator.domain.dto.request.FaultRequest;
import pl.kedziorek.mpkoperator.repository.FaultRepository;
import pl.kedziorek.mpkoperator.service.FaultService;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FaultServiceImpl implements FaultService {
    private final FaultRepository faultRepository;

    @Override
    public Fault saveFault(FaultRequest faultRequest) {
        log.info("Saving new fault to the database");
        Fault fault = Fault.map(faultRequest);

        return faultRepository.save(fault);
    }
}
