package pl.kedziorek.mpkoperator.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kedziorek.mpkoperator.domain.Bus;
import pl.kedziorek.mpkoperator.domain.dto.request.BusRequest;
import pl.kedziorek.mpkoperator.domain.dto.response.BusDetailsResponse;
import pl.kedziorek.mpkoperator.domain.dto.response.BusResponse;
import pl.kedziorek.mpkoperator.domain.dto.response.DataResponse;
import pl.kedziorek.mpkoperator.domain.enums.BusStatus;
import pl.kedziorek.mpkoperator.service.BusService;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.kedziorek.mpkoperator.domain.Bus.mapToBusDetailsResponse;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BusController {
    private final BusService busService;

    @PostMapping("/bus/save")
    public ResponseEntity<?> saveBus(
            @Validated
            @RequestBody
                    BusRequest bus) {
        return ResponseEntity.ok().body(busService.saveOrUpdateBus(bus));
    }

    @GetMapping("/bus/uuid={uuid}")
    public ResponseEntity<BusDetailsResponse> getBus(@PathVariable UUID uuid) {
        Bus bus = busService.findByUuid(uuid);
        return ResponseEntity.ok().body(mapToBusDetailsResponse(bus));
    }

    @PostMapping("/buses/page={page}/size={size}")
    public ResponseEntity<?> getBuses(
            @PathVariable int page,
            @PathVariable int size,
            @RequestBody Map<String, String> params) {
        DataResponse<Bus> busDataResponse = busService.getBuses(params, page, size);
        List<BusResponse> busResponseList = busDataResponse.getData().stream().map(Bus::mapBusToBusResponse).collect(Collectors.toList());
        return ResponseEntity.ok().body(DataResponse.<BusResponse>builder()
                .data(busResponseList)
                .page(busDataResponse.getPage())
                .size(busDataResponse.getSize())
                .build()
        );
    }

    @PutMapping("/bus/{uuid}/{busStatus}")
    public ResponseEntity<Bus> updateBusStatus(@PathVariable BusStatus busStatus, @PathVariable UUID uuid) {
        return ResponseEntity.ok().body(busService.updateBusStatus(busStatus, uuid));
    }

    @GetMapping("/bus/uuid={uuid}/status={status}")
    public ResponseEntity<BusDetailsResponse> changeStatus(@PathVariable UUID uuid, @PathVariable BusStatus status) {
        Bus bus = busService.updateBusStatus(status, uuid);
        return ResponseEntity.ok().body(mapToBusDetailsResponse(bus));
    }
}
