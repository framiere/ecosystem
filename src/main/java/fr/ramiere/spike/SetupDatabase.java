package fr.ramiere.spike;

import fr.ramiere.spike.model.*;
import fr.ramiere.spike.repository.RoleRepository;
import fr.ramiere.spike.repository.SubscriptionRepository;
import fr.ramiere.spike.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static fr.ramiere.spike.model.Subscription.SubscriptionState.active;
import static fr.ramiere.spike.model.Subscription.SubscriptionState.inactive;


@Service
@AllArgsConstructor
public class SetupDatabase {
    @NonNull
    public final SubscriptionRepository subscriptionRepository;
    @NonNull
    public final UserRepository userRepository;
    @NonNull
    public final RoleRepository roleRepository;

    @EventListener
    @Transactional
    public void init(ApplicationReadyEvent ready) {

        Role role = roleRepository.save(Role.builder()
                .name("DBA")
                .description("DBA description")
                .build());

        User user = userRepository.save(
                User.builder()
                        .email("subcription@email.com")
                        .firstName("firstname")
                        .lastName("lastname")
                        .enabled(true)
                        .role(role)
                        .build());

        Team team = Team.builder()
                .name("team")
                .description("test")
                .users(users())
                .user(user)
                .build();

        Ecosystem ecosystem = Ecosystem.builder()
                .name("eco")
                .description("eco description")
                .team(team)
                .build();

        Product product = Product.builder()
                .name("product")
                .description("product description")
                .ecosystem(ecosystem)
                .git(Git.builder().url("http://gitlab.com").branch("master").build())
                .build();

        Subscription activeSubscription = Subscription.builder()
                .name("active")
                .state(active)
                .product(product)
                .team(team)
                .user(user)
                .build();

        Subscription inactiveSubscription = Subscription.builder()
                .name("inactive")
                .state(inactive)
                .product(product)
                .team(team)
                .user(user)
                .build();

        subscriptionRepository.save(Arrays.asList(activeSubscription, inactiveSubscription));
    }

    private final Random random = new Random();

    private Set<User> users() {
        return IntStream.range(1, 10)
                .boxed()
                .map(i -> User.builder()
                        .email("email" + i + "@email.com")
                        .firstName("firstname" + i)
                        .lastName("lastname" + i)
                        .enabled(random.nextBoolean())
                        .build())
                .collect(Collectors.toSet());
    }
}
