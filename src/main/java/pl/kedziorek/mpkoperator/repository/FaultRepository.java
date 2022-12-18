package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziorek.mpkoperator.domain.Fault;

public interface FaultRepository extends JpaRepository<Fault, Long> {

}
