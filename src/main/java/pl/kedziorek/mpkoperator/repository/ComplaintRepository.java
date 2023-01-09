package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.domain.enums.ComplaintStatus;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Optional<Complaint> findByUuid(UUID uuid);

    @Query(value = "" +
            "SELECT c FROM Complaint c " +
            "WHERE (:placeOfEvent is null or c.placeOfEvent LIKE %:placeOfEvent%) " +
            "AND (:nameOfNotifier is null or c.nameOfNotifier LIKE %:nameOfNotifier%) " +
            "AND (:surnameOfNotifier is null or c.surnameOfNotifier LIKE %:surnameOfNotifier%) " +
            "AND (:peselOfNotifier is null or c.peselOfNotifier LIKE %:peselOfNotifier%) " +
            "AND (:createdBy is null or c.createdBy LIKE %:createdBy%) " +
            "AND (cast(:date as date) is null or c.date = :date) " +
            "AND (:complaintStatus is null or c.complaintStatus LIKE :complaintStatus) " +
            "ORDER BY c.createdAt DESC" +
            "")
    Page<Complaint> findAllParams(
            @Param("placeOfEvent") String placeOfEvent,
            @Param("nameOfNotifier") String nameOfNotifier,
            @Param("surnameOfNotifier") String surnameOfNotifier,
            @Param("peselOfNotifier") String peselOfNotifier,
            @Param("createdBy") String createdBy,
            @Param("date") LocalDate date,
            @Param("complaintStatus") ComplaintStatus status,
            Pageable pageable
    );
}
