package fr.ramiere.spike.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "ecosystem")
@Builder(toBuilder = true)
@ToString
@NoArgsConstructor(access = PROTECTED, force = true)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Ecosystem implements Identifiable<Long> {
    @Id
    @Getter
    @GeneratedValue
    public final Long id;
    @Column(unique = true, nullable = false, length = 100)
    @Size(max = 100)
    public final String name;
    @Column(nullable = false, length = 300)
    @Size(max = 300)
    public final String description;
    @OneToMany(cascade = PERSIST)
    @Singular
    public final Set<Team> teams;
    @JsonIgnore
    @Version
    private final Long version;
}
