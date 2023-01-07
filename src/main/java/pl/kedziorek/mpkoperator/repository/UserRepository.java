package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziorek.mpkoperator.domain.User;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsUserByUsername(String username);
    Boolean existsUserByEmail(String email);
    Boolean existsUserByPesel(String pesel);
    Boolean existsUserByPhoneNumber(String pesel);
}
