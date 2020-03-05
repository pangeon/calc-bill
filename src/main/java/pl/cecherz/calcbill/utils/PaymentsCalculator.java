package pl.cecherz.calcbill.utils;

import pl.cecherz.calcbill.exeptions.EmptyFindResultException;
import pl.cecherz.calcbill.exeptions.EntityEmptyContentException;
import pl.cecherz.calcbill.exeptions.EntityNotFoundException;
import pl.cecherz.calcbill.model.db.Owner;
import pl.cecherz.calcbill.model.db.Payments;
import pl.cecherz.calcbill.repositories.OwnerRepository;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentsCalculator {
    public static Double sumOwnerPayments(Integer id, OwnerRepository repository) {
        Double sumOfPayments = 0d;
        Owner owner = repository.findOwnerById(id);
        if (owner == null) throw new EntityNotFoundException(id);

        List<Payments> payments = owner.getPayments();
        if (payments.isEmpty()) throw new EntityEmptyContentException(id);

        for (Payments payment : payments) {
            sumOfPayments += payment.getAmount();
        }
        return sumOfPayments;
    }
    public static Double sumOwnerPaymentsByKind(Integer id, String kind, OwnerRepository repository) {
        Double sumOfPayments = 0d;
        Owner owner = repository.findOwnerById(id);
        if (owner == null) throw new EntityNotFoundException(id);

        List<Payments> sumPaymentsOfKind = owner.getPayments()
                .stream().filter(payments1 -> payments1.getKind().equals(kind))
                .collect(Collectors.toList());
        if (sumPaymentsOfKind.isEmpty()) throw new EmptyFindResultException(id, kind);
        for (Payments payment : sumPaymentsOfKind) {
            sumOfPayments += payment.getAmount();
        }
        return sumOfPayments;
    }
}
