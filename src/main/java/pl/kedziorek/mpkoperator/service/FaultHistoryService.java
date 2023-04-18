package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Fault;
import pl.kedziorek.mpkoperator.domain.FaultHistory;

import java.util.List;
import java.util.UUID;

public interface FaultHistoryService {
    FaultHistory saveFaultInFaultHistory(Fault fault, UUID uuid);
    List<FaultHistory> findAllByUuidOrderByCreatedAt(UUID uuid);
}
