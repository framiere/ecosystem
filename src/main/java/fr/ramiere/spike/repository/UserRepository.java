package fr.ramiere.spike.repository;

import fr.ramiere.spike.model.User;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
@JaversSpringDataAuditable
public interface UserRepository extends CrudRepository<User, Long> {

}
