package pl.kedziorek.mpkoperator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziorek.mpkoperator.domain.UserImage;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
}
