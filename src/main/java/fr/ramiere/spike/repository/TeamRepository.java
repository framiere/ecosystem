package fr.ramiere.spike.repository;

import fr.ramiere.spike.model.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TeamRepository extends CrudRepository<Team, Long> {

}
