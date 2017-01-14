package fr.ramiere.spike.model;

import lombok.*;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "ecosystem")
@Builder(builderMethodName = "ecosystem")
@NoArgsConstructor(access = PROTECTED, force = true)
@AllArgsConstructor
public class Ecosystem implements Identifiable<Long> {
    @Id
    @Getter
    @GeneratedValue
    public final Long id;
    @Column(unique = true)
    public final String name;
    @OneToMany(cascade = PERSIST)
    @Singular
    public final Set<Team> teams;
    @Version
    private final Long version;
}
