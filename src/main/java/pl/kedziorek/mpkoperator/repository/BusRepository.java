package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziorek.mpkoperator.domain.Bus;

import java.util.Optional;
import java.util.UUID;

public interface BusRepository extends JpaRepository<Bus, Long> {
    Optional<Bus> findByUuid(UUID uuid);
}
