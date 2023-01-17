package pl.kedziorek.mpkoperator.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kedziorek.mpkoperator.domain.Comment;
import pl.kedziorek.mpkoperator.domain.Fault;
import pl.kedziorek.mpkoperator.domain.FaultHistory;
import pl.kedziorek.mpkoperator.domain.dto.request.CommentRequest;
import pl.kedziorek.mpkoperator.domain.dto.request.FaultRequest;
import pl.kedziorek.mpkoperator.domain.dto.response.*;
import pl.kedziorek.mpkoperator.domain.enums.FaultStatus;
import pl.kedziorek.mpkoperator.service.FaultHistoryService;
import pl.kedziorek.mpkoperator.service.FaultService;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.kedziorek.mpkoperator.domain.Fault.mapToFaultDetailsResponse;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FaultController {
    private final FaultService faultService;
    private final FaultHistoryService faultHistoryService;

    @PostMapping("/fault/save")
    public ResponseEntity<?> saveFault(
            @Validated
            @RequestBody
            FaultRequest fault) {
        return ResponseEntity.ok().body(faultService.saveOrUpdateFault(fault));
    }

    @GetMapping("/fault/uuid={uuid}")
    public ResponseEntity<Fault> getFault(@PathVariable UUID uuid) {
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

    @PutMapping("/fault/{uuid}/{faultStatus}")
    public ResponseEntity<Fault> updateFault(@PathVariable FaultStatus faultStatus, @PathVariable UUID uuid) {
        return ResponseEntity.ok().body(faultService.updateFaultStatus(faultStatus, uuid));
    }

    @PostMapping("/fault/uuid={uuid}")
    public ResponseEntity<CommentReplay> addComment(@PathVariable UUID uuid, @RequestBody CommentRequest commentRequest) {
        List<Comment> commentList = faultService.createComment(uuid, commentRequest.getContent());
        List<CommentResponse> commentResponses = commentList.stream().map(Comment::mapToCommentResponse).collect(Collectors.toList());
        return ResponseEntity.ok().body(CommentReplay.builder()
                .uuid(uuid)
                .commentResponseList(commentResponses)
                .build());
    }

    @GetMapping("/fault/uuid={uuid}/status={status}")
    public ResponseEntity<FaultDetailsResponse> changeStatus(@PathVariable UUID uuid, @PathVariable FaultStatus status) {
        Fault fault = faultService.updateFaultStatus(status, uuid);
        return ResponseEntity.ok().body(mapToFaultDetailsResponse(fault));
    }

    @GetMapping("/faultHistories/uuid={uuid}")
        public ResponseEntity<List<FaultHistoryResponse>> getFaultHistoryList(@PathVariable UUID uuid) {
        List<FaultHistory> faultHistories = faultHistoryService.findAllByUuidOrderByCreatedAt(uuid);
        return ResponseEntity.ok().body(faultHistories.stream().map(FaultHistory::mapToFaultHistoryResponse).collect(Collectors.toList()));
    }
}
