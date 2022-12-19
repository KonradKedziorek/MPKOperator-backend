package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Fault;
import pl.kedziorek.mpkoperator.domain.FaultHistory;

import java.util.UUID;

public interface FaultHistoryService {
    FaultHistory saveComplaintInFaultHistory(Fault fault, UUID uuid);
}
