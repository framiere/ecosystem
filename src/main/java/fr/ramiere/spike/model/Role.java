package fr.ramiere.spike.model;

import lombok.*;
import org.springframework.hateoas.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import static lombok.AccessLevel.PROTECTED;

@Entity(name = "role")
@Builder(toBuilder = true)
@ToString
@NoArgsConstructor(access = PROTECTED, force = true)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Role implements Identifiable<Long> {
    @Id
    @Getter
    @GeneratedValue
    public final Long id;
    @Column(unique = true, nullable = false, length = 100)
    public final String name;
    @Column(nullable = false, length = 300)
    @Size(max = 100)
    public final String description;
}
