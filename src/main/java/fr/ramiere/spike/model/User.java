package fr.ramiere.spike.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "user")
@Builder(toBuilder = true)
@ToString
@NoArgsConstructor(access = PROTECTED, force = true)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements Identifiable<Long> {
    @Id
    @Getter
    @GeneratedValue
    public final Long id;
    @Column(nullable = false, length = 255)
    @Size(max = 255)
    public final String firstName;
    @Column(nullable = false, length = 255)
    @Size(max = 255)
    public final String lastName;
    @Column(nullable = false, length = 255)
    @Size(max = 255)
    @Email
    public final String email;
    @OneToMany(cascade = PERSIST)
    @Singular
    public final Set<Role> roles;
    @NotNull
    public final Boolean enabled;
    @JsonIgnore
    @Version
    private final Long version;

    public User enable() {
        return toBuilder().enabled(true).build();
    }

    public User disable() {
        return toBuilder().enabled(false).build();
    }
}
