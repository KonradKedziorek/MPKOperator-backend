package pl.kedziorek.mpkoperator.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kedziorek.mpkoperator.domain.dto.request.BusRequest;
import pl.kedziorek.mpkoperator.service.BusService;

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
}
