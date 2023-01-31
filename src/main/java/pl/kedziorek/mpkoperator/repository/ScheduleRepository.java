package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziorek.mpkoperator.domain.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
