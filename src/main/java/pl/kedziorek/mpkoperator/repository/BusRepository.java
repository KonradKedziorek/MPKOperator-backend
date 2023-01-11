package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziorek.mpkoperator.domain.Bus;

public interface BusRepository extends JpaRepository<Bus, Long> {
}
