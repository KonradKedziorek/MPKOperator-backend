package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Fault;
import pl.kedziorek.mpkoperator.domain.dto.request.FaultRequest;

public interface FaultService {
    Fault saveFault(FaultRequest faultRequest);
}
