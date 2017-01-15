package fr.ramiere.spike.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static fr.ramiere.spike.model.Subscription.SubscriptionState.inactive;
import static javax.persistence.CascadeType.PERSIST;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "subscription")
@Builder(toBuilder = true)
@ToString
@NoArgsConstructor(access = PROTECTED, force = true)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Subscription implements Identifiable<Long> {
    @Id
    @Getter
    @GeneratedValue
    public final Long id;
    @Column(unique = true, nullable = false, length = 100)
    public final String name;
    @NotNull
    @ManyToOne(cascade = PERSIST)
    public final Product product;
    @NotNull
    @ManyToOne(cascade = PERSIST)
    public final Team team;
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true) // otherwise first ref as POJO, others as id
    @NotNull
    @ManyToOne(cascade = PERSIST)
    public final User user;
    @NotNull
    public final SubscriptionState state;

    public Subscription disable() {
        return toBuilder().state(inactive).build();
    }

    public enum SubscriptionState {
        active,
        inactive,
        deleted
    }
}
