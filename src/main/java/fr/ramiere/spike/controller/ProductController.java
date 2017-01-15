package fr.ramiere.spike.controller;

import fr.ramiere.spike.controller.SubscriptionController.SubscriptionResourceProcessor;
import fr.ramiere.spike.model.Product;
import fr.ramiere.spike.model.Subscription;
import fr.ramiere.spike.model.Team;
import fr.ramiere.spike.model.User;
import fr.ramiere.spike.repository.SubscriptionRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

import static fr.ramiere.spike.controller.SetupHAL.CURIE_NAMESPACE;
import static fr.ramiere.spike.model.Subscription.SubscriptionState.active;
import static org.javers.repository.jql.QueryBuilder.byInstance;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@RestController
@RequestMapping("/products")
@ExposesResourceFor(Product.class)
@RequiredArgsConstructor
public class ProductController {
    private static final String FORM_PATH = "/form";
    private static final String FORM_REL = CURIE_NAMESPACE + ":form";

    private static final String SUBMIT_PATH = "/submit";
    private static final String SUBMIT_REL = CURIE_NAMESPACE + ":submit";

    private static final String AUDIT_PATH = "/audit";
    private static final String AUDIT_REL = CURIE_NAMESPACE + ":audit";

    private static final String CHANGES_PATH = "/changes";
    private static final String CHANGES_REL = CURIE_NAMESPACE + ":changes";

    @NonNull
    private final Javers javers;
    @NonNull
    private final SubscriptionRepository subscriptionRepository;
    @NonNull
    private final SubscriptionResourceProcessor subscriptionResourceProcessor;

    @GetMapping(path = "/{id}/" + FORM_PATH)
    public HttpEntity<String> form(@PathVariable("id") Product product) {
        return product == null ? notFound().build() : ok("<jsx>form</jsx>");
    }

    @PostMapping(path = "/{id}/" + SUBMIT_PATH + "/{teamId}/{userId}")
    public HttpEntity<Resource<Subscription>> postForm(@PathVariable("id") Product product, @PathVariable("teamId") Team team, @PathVariable("userId") User user, @RequestBody Map<String, String> values) {
        Subscription newSubscription = subscriptionRepository.save(Subscription.builder()
                .name("a")
                .product(product)
                .state(active)
                .team(team)
                .user(user)
                .build());
        return newSubscription == null ? notFound().build() : ok(subscriptionResourceProcessor.process(new Resource<>(newSubscription)));
    }

    @GetMapping(path = "/{id}/" + AUDIT_PATH)
    public HttpEntity<List<Change>> audit(@PathVariable("id") Product product) {
        return product == null ? notFound().build() : ok(javers.findChanges(byInstance(product).build()));
    }

    @GetMapping(path = "/{id}/" + CHANGES_PATH)
    public HttpEntity<String> changes(@NotNull @PathVariable("id") Product product) {
        return product == null ? notFound().build() : ok(javers.processChangeList(audit(product).getBody(), new SimpleTextChangeLog()));
    }

    @Component
    @RequiredArgsConstructor
    public static class ProductResourceProcessor implements ResourceProcessor<Resource<Product>> {

        @NonNull
        private final EntityLinks entityLinks;

        @Override
        public Resource<Product> process(Resource<Product> resource) {
            Product product = resource.getContent();
            resource.add(entityLinks.linkForSingleResource(product).slash(FORM_PATH).withRel(FORM_REL));
            resource.add(entityLinks.linkForSingleResource(product).slash(SUBMIT_PATH).withRel(SUBMIT_REL));
            resource.add(entityLinks.linkForSingleResource(product).slash(AUDIT_PATH).withRel(AUDIT_REL));
            resource.add(entityLinks.linkForSingleResource(product).slash(CHANGES_PATH).withRel(CHANGES_REL));
            return resource;
        }
    }

    @Bean
    public Docket productsApi() {
        return new Docket(SWAGGER_2)
                .groupName("products")
                .apiInfo(new ApiInfoBuilder()
                        .title("Products")
                        .version("1.0")
                        .build())
                .select()
                .paths(regex("/products/.*"))
                .build();
    }
}
