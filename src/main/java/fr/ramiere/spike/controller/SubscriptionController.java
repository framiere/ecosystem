package fr.ramiere.spike.controller;

import fr.ramiere.spike.model.Subscription;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/subscription/{id}")
@ExposesResourceFor(Subscription.class)
@RequiredArgsConstructor
public class SubscriptionController {
    @NonNull
    private final EntityLinks entityLinks;

    @RequestMapping(path = SubscriptionLinks.CANCEL_PATH, method = GET)
    HttpEntity<?> cancel(@PathVariable("id") String id) {
        return ResponseEntity.notFound().header("message", "va chier").build();
    }

    @RequestMapping(path = SubscriptionLinks.LOGS_PATH, method = GET)
    HttpEntity<?> logs(@PathVariable("id") String id) {
        return ResponseEntity.ok("logs");
    }
}
