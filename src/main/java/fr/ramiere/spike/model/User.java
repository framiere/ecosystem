package fr.ramiere.spike.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.Identifiable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

import static lombok.AccessLevel.PROTECTED;

@Entity(name = "user")
@Builder(builderMethodName = "user")
@NoArgsConstructor(access = PROTECTED, force = true)
@AllArgsConstructor
public class User implements Identifiable<Long> {
    @Id
    @Getter
    @GeneratedValue
    public final Long id;
    @NotEmpty
    public final String firstName;
    @NotEmpty
    public final String lastName;
    @Email
    public final String email;
    @Version
    private final Long version;
}
