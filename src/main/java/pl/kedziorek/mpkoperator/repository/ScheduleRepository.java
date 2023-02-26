package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kedziorek.mpkoperator.domain.Schedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query(value = "" +
            "SELECT s FROM Schedule s " +
            "WHERE :name is null or s.name LIKE %:name% " +
            "AND s.createdAt >= :date " +
            "ORDER BY s.date DESC")
    Optional<List<Schedule>> findSchedulesByNameOrderByDateDesc(
            @Param("name") String name,
            @Param("date")LocalDateTime date
            );
    Optional<Schedule> findByUuid(UUID uuid);
}
