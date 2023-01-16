package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kedziorek.mpkoperator.domain.Bus;
import pl.kedziorek.mpkoperator.domain.enums.BusStatus;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface BusRepository extends JpaRepository<Bus, Long> {
    Optional<Bus> findByUuid(UUID uuid);
    Optional<Bus> findByBusNumber(Integer busNumber);

    @Query(value = "" +
            "SELECT b FROM Bus b " +
            "WHERE (cast(:busNumber as int) is null or b.busNumber = :busNumber) " +
            "AND (cast(:mileage as long) is null or b.mileage = :mileage) " +
            "AND (:registrationNumber is null or b.registrationNumber LIKE %:registrationNumber%) " +
            "AND (cast(:firstRegistrationDate as date) is null or b.firstRegistrationDate = :firstRegistrationDate) " +
            "AND (cast(:insuranceExpiryDate as date) is null or b.insuranceExpiryDate = :insuranceExpiryDate) " +
            "AND (cast(:serviceExpiryDate as date) is null or b.serviceExpiryDate = :serviceExpiryDate) " +
            "AND (:busStatus is null or b.busStatus LIKE :busStatus) " +
            "ORDER BY b.createdAt DESC" +
            "")
    Page<Bus> findAllParams(
            @Param("busNumber") Integer busNumber,
            @Param("mileage") Long mileage,
            @Param("registrationNumber") String registrationNumber,
            @Param("firstRegistrationDate") LocalDate firstRegistrationDate,
            @Param("insuranceExpiryDate") LocalDate insuranceExpiryDate,
            @Param("serviceExpiryDate") LocalDate serviceExpiryDate,
            @Param("busStatus") BusStatus status,
            Pageable pageable
    );
}
