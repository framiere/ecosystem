package fr.ramiere.spike.controller;

import fr.ramiere.spike.model.Subscription;
import fr.ramiere.spike.repository.SubscriptionRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.javers.core.Javers;
import org.javers.core.diff.Change;
import org.javers.repository.jql.QueryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static fr.ramiere.spike.controller.SetupHAL.CURIE_NAMESPACE;
import static fr.ramiere.spike.model.Subscription.SubscriptionState.active;
import static fr.ramiere.spike.model.Subscription.SubscriptionState.inactive;
import static springfox.documentation.builders.PathSelectors.regex;

@RestController
@RequestMapping("/subscriptions/{id}")
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

    @NonNull
    private final SubscriptionRepository subscriptionRepository;
    @NonNull
    private final Javers javers;

    @GetMapping(path = CANCEL_PATH)
    public HttpEntity<?> cancel(@PathVariable("id") Subscription subscription) {
        if (subscription == null) {
            return ResponseEntity.notFound().build();
        }
        subscription.state = inactive;
        subscriptionRepository.save(subscription);
        return ResponseEntity.ok(subscription.name + " is now canceled");
    }

    @GetMapping(path = LOGS_PATH)
    public List<String> logs(@PathVariable("id") Subscription subscription) {
        return LongStream.rangeClosed(1, 10)
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    @GetMapping(path = DELETE_PATH)
    public HttpEntity<?> delete(@PathVariable("id") Subscription subscription) {
        if (subscription == null) {
            return ResponseEntity.notFound().build();
        }
        subscriptionRepository.delete(subscription.id);
        return ResponseEntity.ok(subscription.name + " is deleted");
    }

    @GetMapping(path = AUDIT_PATH)
    public HttpEntity<List<Change>> audit(@PathVariable("id") Subscription subscription) {
        if (subscription == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(javers.findChanges(QueryBuilder.byInstance(subscription).build()));
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
            return resource;
        }
    }

    @Bean
    public Docket subscriptionApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("subscriptions")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/subscriptions/.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Subscription")
                .version("2.0")
                .build();
    }
}
