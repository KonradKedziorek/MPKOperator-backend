package pl.kedziorek.mpkoperator.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.domain.Fault;
import pl.kedziorek.mpkoperator.domain.dto.request.FaultRequest;
import pl.kedziorek.mpkoperator.domain.dto.response.ComplaintResponse;
import pl.kedziorek.mpkoperator.domain.dto.response.DataResponse;
import pl.kedziorek.mpkoperator.domain.dto.response.FaultResponse;
import pl.kedziorek.mpkoperator.service.FaultService;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @GetMapping("/fault/{uuid}")
    public ResponseEntity<Fault> getComplaint(@PathVariable UUID uuid) {
        return ResponseEntity.ok().body(faultService.findByUuid(uuid));
    }

    @PostMapping("/fault/page={page}/size={size}")
    public ResponseEntity<?> getFaults(
            @PathVariable int page,
            @PathVariable int size,
            @RequestBody Map<String, String> params) {
        DataResponse<Fault> faultDataResponse = faultService.getFaults(params, page, size);
        List<FaultResponse> faultResponseList = faultDataResponse.getData().stream().map(Fault::responseMap).collect(Collectors.toList());
        return ResponseEntity.ok().body(DataResponse.<FaultResponse>builder()
                .data(faultResponseList)
                .page(faultDataResponse.getPage())
                .size(faultDataResponse.getSize())
                .build()
        );

    }

    @PutMapping("/fault/{uuid}/update")
    public ResponseEntity<Fault> updateFault(@RequestBody Fault fault, @PathVariable UUID uuid) {
        return ResponseEntity.ok().body(faultService.updateFault(fault, uuid));
    }
}
