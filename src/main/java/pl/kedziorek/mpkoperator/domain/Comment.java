package pl.kedziorek.mpkoperator.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import pl.kedziorek.mpkoperator.domain.dto.response.CommentResponse;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @NotBlank
    private String content;

    @ManyToOne
    @JoinColumn(name = "complaint_id")
    @JsonIgnore
    private Complaint complaint;

    @ManyToOne
    @JoinColumn(name = "fault_id")
    @JsonIgnore
    private Fault fault;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Comment comment = (Comment) o;
        return id != null && Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public static CommentResponse mapToCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .createdBy(comment.getCreatedBy())
                .build();
    }
}
