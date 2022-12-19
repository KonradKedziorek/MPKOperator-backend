package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziorek.mpkoperator.domain.FaultHistory;

public interface FaultHistoryRepository extends JpaRepository<FaultHistory, Long> {
}
