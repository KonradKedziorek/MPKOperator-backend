package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kedziorek.mpkoperator.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUuid(UUID uuid);
    Optional<User> findByEmail(String email);
    Boolean existsUserByUsername(String username);
    Boolean existsUserByEmail(String email);
    Boolean existsUserByPesel(String pesel);
    Boolean existsUserByPhoneNumber(String pesel);

    @Query(value = "" +
            "SELECT u FROM User u " +
            "WHERE (:name is null or u.name LIKE %:name%) " +
            "AND (:surname is null or u.surname LIKE %:surname%) " +
            "AND (:username is null or u.username LIKE %:username%) " +
            "AND (:email is null or u.email LIKE %:email%) " +
            "AND (:pesel is null or u.pesel LIKE %:pesel%) " +
            "AND (:phoneNumber is null or u.phoneNumber LIKE %:phoneNumber%) " +
            "ORDER BY u.createdAt DESC" +
            "")
    Page<User> findAllParams(
            @Param("name") String name,
            @Param("surname") String surname,
            @Param("username") String username,
            @Param("email") String email,
            @Param("pesel") String pesel,
            @Param("phoneNumber") String phoneNumber,
            Pageable pageable
    );
}
