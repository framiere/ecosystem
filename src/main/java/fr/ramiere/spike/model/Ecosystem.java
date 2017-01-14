package fr.ramiere.spike.model;

import lombok.*;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "ecosystem")
@Builder(builderMethodName = "ecosystem")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Ecosystem implements Identifiable<Long> {
    @Id
    @Getter
    @GeneratedValue
    public Long id;
    @Column(unique = true)
    public String name;
    @OneToMany(cascade = PERSIST)
    @Singular
    public Set<Team> teams;
}
