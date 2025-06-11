package org.example.model.user;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"id", "externalId"})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    @Column(name = "external_id", unique = true, nullable = false)
    private String externalId = UUID.randomUUID().toString();

    @Column(name = "first_name", nullable = false)
    @NonNull
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NonNull
    private String lastName;
}
