package pl.cecherz.calcbill.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.cecherz.calcbill.model.db.Owner;

import java.util.List;

@Repository
public interface OwnerRepository extends CrudRepository<Owner, Integer> {
    Owner findOwnerById(Integer id);
    List<Owner> findOwnerByName(String name);
    List<Owner> findOwnerBySurname(String surname);
}
