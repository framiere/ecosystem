package fr.ramiere.spike.controller;

import fr.ramiere.spike.model.Subscription;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static fr.ramiere.spike.controller.SetupHAL.*;

@Component
@RequiredArgsConstructor
public class SubscriptionLinks {
    static final String CANCEL = "cancel";
    static final String LOGS = "logs";
    static final String CANCEL_PATH = "/" + CANCEL;
    static final String LOGS_PATH = "/" + LOGS;
    static final String CANCEL_REL = CURIE_NAMESPACE + ":" + CANCEL;
    static final String LOGS_REL = CURIE_NAMESPACE + ":" + LOGS;

    @NonNull
    private final EntityLinks entityLinks;

    Link cancelLink(Subscription subscription) {
        return entityLinks.linkForSingleResource(subscription).slash(CANCEL_PATH).withRel(CANCEL_REL);
    }

    Link logsLink(Subscription subscription) {
        return entityLinks.linkForSingleResource(subscription).slash(LOGS_PATH).withRel(LOGS_REL);
    }
}
