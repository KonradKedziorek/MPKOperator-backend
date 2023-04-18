package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Bus;
import pl.kedziorek.mpkoperator.domain.dto.request.BusRequest;
import pl.kedziorek.mpkoperator.domain.dto.response.DataResponse;
import pl.kedziorek.mpkoperator.domain.enums.BusStatus;

import java.util.Map;
import java.util.UUID;

public interface BusService {
    Bus findByUuid(UUID uuid);
    Bus saveOrUpdateBus(BusRequest busRequest);
    DataResponse<Bus> getBuses(Map<String, String> params, int page, int size);
    Bus updateBusStatus(BusStatus busStatus, UUID uuid);
}
