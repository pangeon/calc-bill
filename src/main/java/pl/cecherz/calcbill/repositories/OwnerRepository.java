package pl.cecherz.calcbill.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.cecherz.calcbill.model.Owner;

public interface OwnerRepository extends CrudRepository<Owner, Integer> {}
