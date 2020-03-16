package pl.cecherz.calcbill.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.cecherz.calcbill.model.db.Owner;
import pl.cecherz.calcbill.model.db.Payments;

import java.util.List;

@Repository
public interface PaymentsRepository extends CrudRepository<Payments, Integer> {

    Payments findPaymentById(Integer id);
    List<Payments> findPaymentsByKind(String kindOfPayments);
    List<Payments> findPaymentsByAmountBetween(Double min, Double max);
    List<Payments> findPaymentsByOwnerId(Owner ownerId);
}
