package pl.kedziorek.mpkoperator.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kedziorek.mpkoperator.domain.Comment;
import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.domain.dto.request.CommentRequest;
import pl.kedziorek.mpkoperator.domain.dto.request.ComplaintRequest;
import pl.kedziorek.mpkoperator.domain.dto.response.*;
import pl.kedziorek.mpkoperator.domain.enums.ComplaintStatus;
import pl.kedziorek.mpkoperator.service.ComplaintService;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.kedziorek.mpkoperator.domain.Complaint.mapToComplaintDetailsResponse;

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
        return ResponseEntity.ok().body(complaintService.saveOfUpdateComplaint(complaint));
    }

    @PostMapping("/complaints/page={page}/size={size}")
    public ResponseEntity<?> getComplaints(
            @PathVariable int page,
            @PathVariable int size,
            @RequestBody Map<String, String> params) {
        DataResponse<Complaint> complaintDataResponse = complaintService.getComplaints(params, page, size);
        List<ComplaintResponse> complaintResponseList = complaintDataResponse.getData().stream().map(Complaint::map2).collect(Collectors.toList());
        return ResponseEntity.ok().body(DataResponse.<ComplaintResponse>builder()
                .data(complaintResponseList)
                .page(complaintDataResponse.getPage())
                .size(complaintDataResponse.getSize())
                .build()
        );
    }

    @GetMapping("/complaint/uuid={uuid}")
    public ResponseEntity<ComplaintDetailsResponse> getComplaint(@PathVariable UUID uuid) {
        Complaint complaint = complaintService.findByUuid(uuid);
        return ResponseEntity.ok().body(mapToComplaintDetailsResponse(complaint));
    }

    @PutMapping("/complaint/{uuid}/{complaintStatus}")
    public ResponseEntity<Complaint> updateComplaint(@PathVariable ComplaintStatus complaintStatus, @PathVariable UUID uuid) {
        return ResponseEntity.ok().body(complaintService.updateComplaintStatus(complaintStatus, uuid));
    }

    @PostMapping("/complaint/uuid={uuid}")
    public ResponseEntity<CommentReplay> addComment(@PathVariable UUID uuid, @RequestBody CommentRequest commentRequest) {
        List<Comment> commentList = complaintService.createComment(uuid, commentRequest.getContent());
        List<CommentResponse> commentResponses = commentList.stream().map(Comment::mapToCommentResponse).collect(Collectors.toList());
        return ResponseEntity.ok().body(CommentReplay.builder()
                .uuid(uuid)
                .commentResponseList(commentResponses)
                .build());

    }
}
