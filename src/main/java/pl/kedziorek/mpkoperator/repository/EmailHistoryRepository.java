package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziorek.mpkoperator.domain.EmailHistory;

public interface EmailHistoryRepository extends JpaRepository<EmailHistory, Long> {
}
