package pl.kedziorek.mpkoperator.domain;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "user_image", schema = "public")
public class UserImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    private String name;

    @Lob
    private byte[] bytes;
}
