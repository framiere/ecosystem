package fr.ramiere.spike.repository;

import fr.ramiere.spike.model.Product;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
@JaversSpringDataAuditable
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

}
