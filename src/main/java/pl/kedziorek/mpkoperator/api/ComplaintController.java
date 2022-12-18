package pl.kedziorek.mpkoperator.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.domain.dto.request.ComplaintRequest;
import pl.kedziorek.mpkoperator.service.ComplaintService;

import java.util.Map;
import java.util.UUID;

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

    @PostMapping("/complaints/page={page}/size={size}")
    public ResponseEntity<?> getComplaints(
            @PathVariable int page,
            @PathVariable int size,
            @RequestBody Map<String, String> params) {
        return ResponseEntity.ok().body(
                complaintService.getComplaints(params, page, size)
        );
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
