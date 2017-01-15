package fr.ramiere.spike.controller;

import fr.ramiere.spike.model.Subscription;
import fr.ramiere.spike.repository.ProductRepository;
import fr.ramiere.spike.repository.SubscriptionRepository;
import fr.ramiere.spike.repository.TeamRepository;
import fr.ramiere.spike.repository.UserRepository;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.javers.core.Javers;
import org.javers.core.changelog.SimpleTextChangeLog;
import org.javers.core.diff.Change;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.spring.web.plugins.Docket;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static fr.ramiere.spike.controller.SetupHAL.CURIE_NAMESPACE;
import static fr.ramiere.spike.model.Subscription.SubscriptionState.active;
import static fr.ramiere.spike.model.Subscription.SubscriptionState.deleted;
import static org.javers.repository.jql.QueryBuilder.byInstance;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@RestController
@RequestMapping("/subscriptions")
@ExposesResourceFor(Subscription.class)
@RequiredArgsConstructor
public class SubscriptionController {
    private static final String CANCEL_PATH = "/cancel";
    private static final String CANCEL_REL = CURIE_NAMESPACE + ":cancel";

    private static final String LOGS_PATH = "/logs";
    private static final String LOGS_REL = CURIE_NAMESPACE + ":logs";

    private static final String DELETE_PATH = "/delete";
    private static final String DELETE_REL = CURIE_NAMESPACE + ":delete";

    private static final String AUDIT_PATH = "/audit";
    private static final String AUDIT_REL = CURIE_NAMESPACE + ":audit";

    private static final String CHANGES_PATH = "/changes";
    private static final String CHANGES_REL = CURIE_NAMESPACE + ":changes";

    @NonNull
    private final SubscriptionRepository subscriptionRepository;
    @NonNull
    private final ProductRepository productRepository;
    @NonNull
    private final TeamRepository teamRepository;
    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final Javers javers;
    @NonNull
    private final SubscriptionResourceProcessor subscriptionResourceProcessor;

    @Data
    public static class SubscriptionForm {
        @NotEmpty
        public final String name;
        @NotNull
        public final Long productId;
        @NotNull
        public final Long teamId;
        @NotNull
        public final Long userId;
    }

    @PostMapping(path = "/")
    public HttpEntity<Resource<Subscription>> create(@Valid @RequestBody(required = true) SubscriptionForm subscriptionForm) {
        Subscription subscription = Subscription.builder()
                .name(subscriptionForm.name)
                .product(productRepository.findOne(subscriptionForm.productId))
                .team(teamRepository.findOne(subscriptionForm.teamId))
                .user(userRepository.findOne(subscriptionForm.userId))
                .state(active)
                .build();
        return ok(subscriptionResourceProcessor.process(new Resource<>(subscriptionRepository.save(subscription))));
    }

    @GetMapping(path = "/{id}/" + CANCEL_PATH)
    public HttpEntity<Resource<Subscription>> cancel(@PathVariable("id") Subscription subscription) {
        return subscription == null ? notFound().build() : ok(subscriptionResourceProcessor.process(new Resource<>(subscriptionRepository.save(subscription.disable()))));
    }

    @GetMapping(path = "/{id}/" + LOGS_PATH)
    public List<String> logs(@PathVariable("id") Subscription subscription) {
        return LongStream.rangeClosed(1, 10)
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}/" + DELETE_PATH)
    public HttpEntity<?> delete(@PathVariable("id") Subscription subscription) {
        if (subscription == null || subscription.state == deleted) {
            return notFound().build();
        }
        subscriptionRepository.delete(subscription.id);
        return ok(subscription.name + " is deleted");
    }

    @GetMapping(path = "/{id}/" + AUDIT_PATH)
    public HttpEntity<List<Change>> audit(@PathVariable("id") Subscription subscription) {
        return subscription == null ? notFound().build() : ok(javers.findChanges(byInstance(subscription).build()));
    }

    @GetMapping(path = "/{id}/" + CHANGES_PATH)
    public HttpEntity<String> changes(@PathVariable("id") Subscription subscription) {
        return subscription == null ? notFound().build() : ok(javers.processChangeList(audit(subscription).getBody(), new SimpleTextChangeLog()));
    }

    @ExceptionHandler(RuntimeException.class)
    public String onRuntimeException(RuntimeException e) {
        return e.getMessage();
    }

    @Component
    @RequiredArgsConstructor
    public static class SubscriptionResourceProcessor implements ResourceProcessor<Resource<Subscription>> {

        @NonNull
        private final EntityLinks entityLinks;

        @Override
        public Resource<Subscription> process(Resource<Subscription> resource) {
            Subscription subscription = resource.getContent();
            if (subscription.state == active) {
                resource.add(entityLinks.linkForSingleResource(subscription).slash(CANCEL_PATH).withRel(CANCEL_REL));
            }
            resource.add(entityLinks.linkForSingleResource(subscription).slash(DELETE_PATH).withRel(DELETE_REL));
            resource.add(entityLinks.linkForSingleResource(subscription).slash(LOGS_PATH).withRel(LOGS_REL));
            resource.add(entityLinks.linkForSingleResource(subscription).slash(AUDIT_PATH).withRel(AUDIT_REL));
            resource.add(entityLinks.linkForSingleResource(subscription).slash(CHANGES_PATH).withRel(CHANGES_REL));
            return resource;
        }
    }

    @Bean
    public Docket subscriptionsApi() {
        return new Docket(SWAGGER_2)
                .groupName("subscriptions")
                .apiInfo(new ApiInfoBuilder()
                        .title("Subscription")
                        .version("1.0")
                        .build())
                .select()
                .paths(regex("/subscriptions/.*"))
                .build();
    }
}
