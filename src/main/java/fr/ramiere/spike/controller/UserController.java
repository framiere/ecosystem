package fr.ramiere.spike.controller;

import fr.ramiere.spike.model.User;
import fr.ramiere.spike.repository.UserRepository;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

import static fr.ramiere.spike.controller.SetupHAL.CURIE_NAMESPACE;
import static org.javers.repository.jql.QueryBuilder.byInstance;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@RestController
@RequestMapping("/users/{id}")
@ExposesResourceFor(User.class)
@RequiredArgsConstructor
public class UserController {
    private static final String ENABLE_PATH = "/enable";
    private static final String ENABLE_REL = CURIE_NAMESPACE + ":enable";

    private static final String DISABLE_PATH = "/disable";
    private static final String DISABLE_REL = CURIE_NAMESPACE + ":disable";

    private static final String AUDIT_PATH = "/audit";
    private static final String AUDIT_REL = CURIE_NAMESPACE + ":audit";

    private static final String CHANGES_PATH = "/changes";
    private static final String CHANGES_REL = CURIE_NAMESPACE + ":changes";

    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final Javers javers;

    @GetMapping(path = ENABLE_PATH)
    public HttpEntity<User> enable(@PathVariable("id") User user) {
        return user == null ? notFound().build() : ok(userRepository.save(user.enable()));
    }

    @GetMapping(path = DISABLE_PATH)
    public HttpEntity disable(@PathVariable("id") User user) {
        return user == null ? notFound().build() : ok(userRepository.save(user.disable()));
    }

    @GetMapping(path = AUDIT_PATH)
    public HttpEntity<List<Change>> audit(@PathVariable("id") User user) {
        return user == null ? notFound().build() : ok(javers.findChanges(byInstance(user).build()));
    }

    @GetMapping(path = CHANGES_PATH)
    public HttpEntity<String> changes(@PathVariable("id") User user) {
        return user == null ? notFound().build() : ok(javers.processChangeList(audit(user).getBody(), new SimpleTextChangeLog()));
    }

    @Component
    @RequiredArgsConstructor
    public static class UserResourceProcessor implements ResourceProcessor<Resource<User>> {

        @NonNull
        private final EntityLinks entityLinks;

        @Override
        public Resource<User> process(Resource<User> resource) {
            User user = resource.getContent();
            if (user.enabled) {
                resource.add(entityLinks.linkForSingleResource(user).slash(DISABLE_PATH).withRel(DISABLE_REL));
            } else {
                resource.add(entityLinks.linkForSingleResource(user).slash(ENABLE_PATH).withRel(ENABLE_REL));
            }
            resource.add(entityLinks.linkForSingleResource(user).slash(AUDIT_PATH).withRel(AUDIT_REL));
            resource.add(entityLinks.linkForSingleResource(user).slash(CHANGES_PATH).withRel(CHANGES_REL));
            return resource;
        }
    }

    @Bean
    public Docket subscriptionApi() {
        return new Docket(SWAGGER_2)
                .groupName("user")
                .apiInfo(new ApiInfoBuilder()
                        .title("User")
                        .version("2.0")
                        .build())
                .select()
                .paths(regex("/user/.*"))
                .build();
    }

}
