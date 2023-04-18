package pl.kedziorek.mpkoperator.domain.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CommentReplay {
    private UUID uuid;
    private List<CommentResponse> commentResponseList;
}
