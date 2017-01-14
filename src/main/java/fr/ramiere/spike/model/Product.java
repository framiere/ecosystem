package fr.ramiere.spike.model;

import lombok.*;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "product")
@Builder(toBuilder = true)
@NoArgsConstructor(access = PROTECTED, force = true)
@AllArgsConstructor
public class Product implements Identifiable<Long> {
    @Id
    @Getter
    @GeneratedValue
    public final Long id;
    @Column(unique = true)
    public final String name;
    @OneToMany(cascade = PERSIST)
    @Singular
    public final Set<Ecosystem> ecosystems;
    @NotNull
    private final Git git;
    @Version
    private final Long version;
}
