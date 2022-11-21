package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziorek.mpkoperator.domain.Complaint;

import java.util.List;
import java.util.Optional;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Optional<List<Complaint>> findByPeselOfNotifier(String pesel);

}
