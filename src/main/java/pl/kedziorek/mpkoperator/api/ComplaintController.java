package pl.kedziorek.mpkoperator.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kedziorek.mpkoperator.config.exception.ResourceNotFoundException;
import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.repository.ComplaintRepository;
import pl.kedziorek.mpkoperator.service.ComplaintService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ComplaintController {
    private final ComplaintService complaintService;
    private final ComplaintRepository complaintRepository;

    @PostMapping("/complaint/save")
    public ResponseEntity<Complaint> saveComplaint(
            @Validated
            @RequestBody
                    Complaint complaint) {
        return ResponseEntity.ok().body(complaintService.saveComplaint(complaint));
    }

    @GetMapping("/complaint/all")
    public ResponseEntity<List<Complaint>> getComplaints() {
        return ResponseEntity.ok().body(complaintService.getAllComplaints());
    }

    @GetMapping("/complaint/{uuid}")
    public ResponseEntity<Complaint> getComplaint(@PathVariable UUID uuid) {
        return ResponseEntity.ok().body(complaintRepository.findByUuid(uuid).orElseThrow(() ->
                new ResourceNotFoundException("Complaint not found in database")));
    }

    //TODO Searching all complaints of one notifier (methods are ready to use)
}
