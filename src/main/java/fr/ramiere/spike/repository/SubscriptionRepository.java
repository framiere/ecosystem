package fr.ramiere.spike.repository;

import fr.ramiere.spike.model.Subscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

}
