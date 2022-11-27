package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziorek.mpkoperator.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
