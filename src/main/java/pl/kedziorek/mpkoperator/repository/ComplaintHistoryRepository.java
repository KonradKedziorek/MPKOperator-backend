package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziorek.mpkoperator.domain.ComplaintHistory;

import java.util.List;
import java.util.UUID;

public interface ComplaintHistoryRepository extends JpaRepository<ComplaintHistory, Long> {
    List<ComplaintHistory> findAllByUuidOrderByCreatedAtDesc(UUID uuid);
}
