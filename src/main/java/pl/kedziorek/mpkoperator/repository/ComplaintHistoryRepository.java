package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.domain.ComplaintHistory;

import java.util.Optional;
import java.util.UUID;

public interface ComplaintHistoryRepository extends JpaRepository<ComplaintHistory, Long> {
}
