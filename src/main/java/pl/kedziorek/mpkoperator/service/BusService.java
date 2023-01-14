package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Bus;
import pl.kedziorek.mpkoperator.domain.dto.request.BusRequest;

import java.util.UUID;

public interface BusService {
    Bus findByUuid(UUID uuid);
    Bus saveOrUpdateBus(BusRequest busRequest);
}
