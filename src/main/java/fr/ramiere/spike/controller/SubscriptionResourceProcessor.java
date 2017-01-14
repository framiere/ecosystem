package fr.ramiere.spike.controller;

import fr.ramiere.spike.model.Subscription;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class SubscriptionResourceProcessor implements ResourceProcessor<Resource<Subscription>> {

    @NonNull
    private final SubscriptionLinks subcriptionLinks;

    @Override
    public Resource<Subscription> process(Resource<Subscription> resource) {
        Subscription subscription = resource.getContent();
        if (subscription.state == Subscription.SubscriptionState.active) {
            resource.add(subcriptionLinks.cancelLink(subscription));
        }
        resource.add(subcriptionLinks.logsLink(subscription));
        return resource;
    }
}
