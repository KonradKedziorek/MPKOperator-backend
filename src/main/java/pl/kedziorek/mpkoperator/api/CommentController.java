package pl.kedziorek.mpkoperator.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kedziorek.mpkoperator.config.exception.ResourceNotFoundException;
import pl.kedziorek.mpkoperator.domain.Comment;
import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.repository.ComplaintRepository;
import pl.kedziorek.mpkoperator.service.CommentService;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final ComplaintRepository complaintRepository;
    private final CommentService commentService;

    @PostMapping("/complaint/{uuid}/addComment")
    public ResponseEntity<Comment> addCommentToComplaint(@RequestBody Comment comment, @PathVariable UUID uuid) {
        Complaint complaint = complaintRepository.findByUuid(uuid)
                .orElseThrow(()-> new ResourceNotFoundException(
                        String.format("Complaint with uuid: %s not found in the database", uuid)
                ));
        comment.setComplaint(complaint);
        complaint.getComments().add(comment);

        return ResponseEntity.ok().body(commentService.saveComment(comment));
    }
}
