package fr.ramiere.spike.repository;

import fr.ramiere.spike.model.Ecosystem;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
@JaversSpringDataAuditable
public interface EcosystemRepository extends PagingAndSortingRepository<Ecosystem, Long> {

}
