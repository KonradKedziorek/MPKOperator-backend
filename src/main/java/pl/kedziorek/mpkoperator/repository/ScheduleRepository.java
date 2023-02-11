package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kedziorek.mpkoperator.domain.Schedule;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query(value = "" +
            "SELECT s FROM Schedule s " +
            "WHERE :name is null or s.name LIKE %:name% " +
            "ORDER BY s.date DESC")
    Optional<List<Schedule>> findSchedulesByNameOrderByDateDesc(@Param("name") String name);
    Optional<Schedule> findByUuid(UUID uuid);
    @Query(value = "" +
            "SELECT s FROM Schedule s " +
            "WHERE :name is null or (upper(s.name)) LIKE %:name% " +
            "ORDER BY s.date DESC")
    Page<Schedule> findAllParamsByName(
            @Param("name") String name,
            Pageable pageable
    );
}
