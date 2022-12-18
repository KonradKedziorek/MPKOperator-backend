package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kedziorek.mpkoperator.domain.Fault;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface FaultRepository extends JpaRepository<Fault, Long> {
    Optional<Fault> findByUuid(UUID uuid);

    @Query(value = "" +
            "SELECT f FROM Fault f " +
            "WHERE (:placeOfEvent is null or upper(f.placeOfEvent) LIKE %:placeOfEvent%) " +
            "AND (:description is null or upper(f.description) LIKE %:description%) " +
            "AND (:createdBy is null or upper(f.createdBy) LIKE %:createdBy%) " +
            "AND (cast(:date as date) is null or f.date = :date) " +
            "ORDER BY f.createdAt DESC " +
            "")
    Page<Fault> findAllParams(
            @Param("placeOfEvent") String placeOfEvent,
            @Param("description") String description,
            @Param("createdBy") String createdBy,
            @Param("date") LocalDate date,
            Pageable pageable
    );
}
