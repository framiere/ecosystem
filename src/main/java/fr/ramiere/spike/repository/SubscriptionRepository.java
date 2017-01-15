package fr.ramiere.spike.repository;

import fr.ramiere.spike.model.Subscription;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
@JaversSpringDataAuditable
public interface SubscriptionRepository extends PagingAndSortingRepository<Subscription, Long> {

}
