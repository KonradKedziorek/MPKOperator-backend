/**
 * 08 sty 2023 16:43:02
 */
package pl.kedziorek.mpkoperator.domain.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * @author cchojnowski
 */
@Data
@Builder
public class CommentReplay {
    private UUID uuid;
    private List<CommentResponse> commentResponseList;
}
