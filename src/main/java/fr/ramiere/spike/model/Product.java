package fr.ramiere.spike.model;

import lombok.*;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "product")
@Builder(toBuilder = true)
@ToString
@NoArgsConstructor(access = PROTECTED, force = true)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Product implements Identifiable<Long> {
    @Id
    @Getter
    @GeneratedValue
    public final Long id;
    @Column(unique = true, nullable = false, length = 100)
    public final String name;
    @Column(nullable = false, length = 300)
    @Size(max = 100)
    public final String description;
    @OneToMany(cascade = PERSIST)
    @Singular
    public final Set<Ecosystem> ecosystems;
    @NotNull
    private final Git git;
}
