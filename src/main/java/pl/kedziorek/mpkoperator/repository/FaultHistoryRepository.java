package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziorek.mpkoperator.domain.FaultHistory;

import java.util.List;
import java.util.UUID;

public interface FaultHistoryRepository extends JpaRepository<FaultHistory, Long> {
    List<FaultHistory> findAllByUuidOrderByCreatedAtDesc(UUID uuid);
}
