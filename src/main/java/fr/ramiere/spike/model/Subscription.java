package fr.ramiere.spike.model;

import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.CascadeType.PERSIST;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "subscription")
@Builder(builderMethodName = "subscription", toBuilder = true)
@NoArgsConstructor(access = PROTECTED, force = true)
@AllArgsConstructor
@ToString
public class Subscription implements Identifiable<Long> {
    @Id
    @Getter
    @GeneratedValue
    public final Long id;
    @NotEmpty
    public final String name;
    @ManyToOne(cascade = PERSIST)
    public final Product product;
    @ManyToOne(cascade = PERSIST)
    public final Team team;
    @ManyToOne(cascade = PERSIST)
    public final User user;
    @NotNull
    public final SubscriptionState state;
    @Version
    private final Long version;

    public Subscription disable() {
        return toBuilder().state(SubscriptionState.inactive).build();
    }

    public enum SubscriptionState {
        active,
        inactive,
        deleted
    }
}
