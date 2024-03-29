package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kedziorek.mpkoperator.domain.Fault;
import pl.kedziorek.mpkoperator.domain.FaultHistory;
import pl.kedziorek.mpkoperator.repository.FaultHistoryRepository;
import pl.kedziorek.mpkoperator.service.FaultHistoryService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FaultHistoryServiceImpl implements FaultHistoryService {
    private final FaultHistoryRepository faultHistoryRepository;

    @Override
    public FaultHistory saveFaultInFaultHistory(Fault fault, UUID uuid) {
        log.info("Saving new fault in fault history to the database");
        FaultHistory faultHistory = FaultHistory.createFaultHistory(fault, uuid);
        return faultHistoryRepository.save(faultHistory);
    }

    @Override
    public List<FaultHistory> findAllByUuidOrderByCreatedAt(UUID uuid) {
        return faultHistoryRepository.findAllByUuidOrderByCreatedAtDesc(uuid);
    }
}
