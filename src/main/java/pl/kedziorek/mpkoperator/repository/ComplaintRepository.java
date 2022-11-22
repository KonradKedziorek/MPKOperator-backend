package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziorek.mpkoperator.domain.Complaint;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Optional<List<Complaint>> findByPeselOfNotifier(String pesel);
    Optional<Complaint> findByUuid(UUID uuid);
}
