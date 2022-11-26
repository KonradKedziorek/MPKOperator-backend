package pl.kedziorek.mpkoperator.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kedziorek.mpkoperator.config.exception.ResourceNotFoundException;
import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.domain.dto.ComplaintRequest;
import pl.kedziorek.mpkoperator.domain.dto.ComplaintResponse;
import pl.kedziorek.mpkoperator.repository.ComplaintRepository;
import pl.kedziorek.mpkoperator.service.ComplaintService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ComplaintController {
    private final ComplaintService complaintService;

    @PostMapping("/complaint/save")
    public ResponseEntity<?> saveComplaint(
            @Validated
            @RequestBody
                    ComplaintRequest complaint) {
        return ResponseEntity.ok().body(complaintService.saveComplaint(complaint));
    }

    @GetMapping("/complaint/all")
    public ResponseEntity<List<ComplaintResponse>> getComplaints() {
        return ResponseEntity.ok().body(complaintService.getAllComplaints().stream().map(ComplaintResponse::map)
                .collect(Collectors.toList()));
    }

    @GetMapping("/complaint/{uuid}")
    public ResponseEntity<Complaint> getComplaint(@PathVariable UUID uuid) {
        return ResponseEntity.ok().body(complaintService.findByUuid(uuid));
    }

    @PutMapping("/complaint/{uuid}/update")
    public ResponseEntity<Complaint> updateComplaint(@RequestBody Complaint complaint, @PathVariable UUID uuid) {
        return ResponseEntity.ok().body(complaintService.updateComplaint(complaint, uuid));
    }
}
