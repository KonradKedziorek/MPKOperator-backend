package pl.kedziorek.mpkoperator.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kedziorek.mpkoperator.config.exception.ResourceNotFoundException;
import pl.kedziorek.mpkoperator.domain.Comment;
import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.domain.Fault;
import pl.kedziorek.mpkoperator.repository.ComplaintRepository;
import pl.kedziorek.mpkoperator.repository.FaultRepository;
import pl.kedziorek.mpkoperator.service.CommentService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final ComplaintRepository complaintRepository;
    private final FaultRepository faultRepository;
    private final CommentService commentService;

    @PostMapping("/complaint/{uuid}/comment")
    public ResponseEntity<Comment> addCommentToComplaint(@RequestBody Comment comment, @PathVariable UUID uuid) {
        Complaint complaint = complaintRepository.findByUuid(uuid)
                .orElseThrow(()-> new ResourceNotFoundException(
                        String.format("Complaint with uuid: %s not found in the database", uuid)
                ));
        comment.setComplaint(complaint);
        complaint.getComments().add(comment);

        return ResponseEntity.ok().body(commentService.saveComment(comment));
    }

    @GetMapping("/complaint/{uuid}/comments")
    public ResponseEntity<List<Comment>> getAllCommentsOfComplaint(@PathVariable UUID uuid) {
        return ResponseEntity.ok().body(commentService.getAllCommentsOfComplaint(uuid));
    }

    @PostMapping("/fault/{uuid}/comment")
    public ResponseEntity<Comment> addCommentToFault(@RequestBody Comment comment, @PathVariable UUID uuid) {
        Fault fault = faultRepository.findByUuid(uuid)
                .orElseThrow(()-> new ResourceNotFoundException(
                        String.format("Fault with uuid: %s not found in the database", uuid)
                ));
        comment.setFault(fault);
        fault.getComments().add(comment);

        return ResponseEntity.ok().body(commentService.saveComment(comment));
    }

    @GetMapping("/fault/{uuid}/comments")
    public ResponseEntity<List<Comment>> getAllCommentsOfFault(@PathVariable UUID uuid) {
        return ResponseEntity.ok().body(commentService.getAllCommentsOfFault(uuid));
    }
}
