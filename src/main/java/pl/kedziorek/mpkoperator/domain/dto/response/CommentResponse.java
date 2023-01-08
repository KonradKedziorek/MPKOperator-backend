/**
 * 08 sty 2023 10:29:06
 */
package pl.kedziorek.mpkoperator.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * @author cchojnowski
 */
@Data
@Builder
public class CommentResponse {
    private String createdBy;
    private LocalDateTime createdAt;
    private String content;
}
