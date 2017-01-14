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
@Builder(builderMethodName = "product")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Product implements Identifiable<Long> {
    @Id
    @Getter
    @GeneratedValue
    public Long id;
    @Column(unique = true)
    public String name;
    public int version;
    @OneToMany(cascade = PERSIST)
    @Singular
    public Set<Ecosystem> ecosystems;
    @URL
    @NotEmpty
    public String git_url;
    @NotEmpty
    @Column
    public String git_branch;
}
