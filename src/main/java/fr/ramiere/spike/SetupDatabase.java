package fr.ramiere.spike;

import fr.ramiere.spike.model.*;
import fr.ramiere.spike.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class SetupDatabase {
    public final SubscriptionRepository subscriptionRepository;

    @EventListener
    public void init(ApplicationReadyEvent ready) {
        Team team = Team.team().name("team").users(users()).build();

        Ecosystem ecosystem = Ecosystem.ecosystem().name("eco").team(team).build();

        Product product = Product.product()
                .name("product")
                .ecosystem(ecosystem)
                .git_url("http://gitlab.com")
                .git_branch("master")
                .build();

        Subscription activeSubscription = Subscription.subscription()
                .name("active")
                .state(Subscription.SubscriptionState.active)
                .product(product)
                .team(team)
                .build();

        Subscription inactiveSubscription = Subscription.subscription()
                .name("inactive")
                .state(Subscription.SubscriptionState.inactive)
                .product(product)
                .team(team)
                .build();

        subscriptionRepository.save(Arrays.asList(activeSubscription, inactiveSubscription));
    }

    private Set<User> users() {
        return IntStream.range(1, 10)
                .boxed()
                .map(i -> User.user().email("email" + i + "@email.com").firstName("firstname" + i).lastName("lastname" + i).build())
                .collect(Collectors.toSet());
    }
}
