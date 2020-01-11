package pl.cecherz.calcbill.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.cecherz.calcbill.model.Payments;

public interface PaymentsRepository extends CrudRepository<Payments, Integer> {}
