package pl.cecherz.calcbill.utils;

import pl.cecherz.calcbill.model.db.Payments;
import pl.cecherz.calcbill.repositories.OwnerRepository;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentsCalculator {
    public static Double sumOwnerPayments(Integer id, OwnerRepository repository) {
        Double sumOfPayments = 0d;
        List<Payments> payments = repository.findOwnerById(id).getPayments();
        for (Payments payment : payments) {
            sumOfPayments += payment.getAmount();
        }
        return sumOfPayments;
    }
    public static Double sumOwnerPaymentsByKind(Integer id, String kind, OwnerRepository repository) {
        Double sumOfPayments = 0d;
        List<Payments> sumPaymentsOfKind = repository.findOwnerById(id).getPayments()
                .stream().filter(payments1 -> payments1.getKind().equals(kind))
                .collect(Collectors.toList());
        for (Payments payment : sumPaymentsOfKind) {
            sumOfPayments += payment.getAmount();
        }
        return sumOfPayments;
    }
}
