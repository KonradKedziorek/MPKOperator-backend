package pl.kedziorek.mpkoperator.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kedziorek.mpkoperator.domain.dto.request.FaultRequest;
import pl.kedziorek.mpkoperator.service.FaultService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FaultController {
    private final FaultService faultService;

    @PostMapping("/fault/save")
    public ResponseEntity<?> saveFault(
            @Validated
            @RequestBody
            FaultRequest fault) {
        return ResponseEntity.ok().body(faultService.saveFault(fault));
    }
}
