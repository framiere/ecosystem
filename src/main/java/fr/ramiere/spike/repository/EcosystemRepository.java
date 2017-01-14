package fr.ramiere.spike.repository;

import fr.ramiere.spike.model.Ecosystem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EcosystemRepository extends CrudRepository<Ecosystem, Long> {

}
