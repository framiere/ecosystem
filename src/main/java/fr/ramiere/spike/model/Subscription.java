package fr.ramiere.spike.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Identifiable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import static javax.persistence.CascadeType.PERSIST;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "subscription")
@Builder(builderMethodName = "subscription")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Subscription implements Identifiable<Long> {
    @Id
    @Getter
    @GeneratedValue
    public Long id;
    @ManyToOne(cascade = PERSIST)
    public Product product;
    @ManyToOne(cascade = PERSIST)
    public Team team;
    @ManyToOne(cascade = PERSIST)
    public User user;
}
