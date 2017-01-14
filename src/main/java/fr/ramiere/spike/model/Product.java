package fr.ramiere.spike.model;

import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "product")
@Builder(builderMethodName = "product", toBuilder = true)
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
    @URL
    @NotEmpty
    public final String git_url;
    @NotEmpty
    @Column
    public final String git_branch;
    @Version
    private final Long version;
}
