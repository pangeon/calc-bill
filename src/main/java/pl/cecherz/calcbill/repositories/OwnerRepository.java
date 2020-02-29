package pl.cecherz.calcbill.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.cecherz.calcbill.model.db.Owner;

@Repository
public interface OwnerRepository extends CrudRepository<Owner, Integer> {
    Owner findOwnerById(Integer id);
}
