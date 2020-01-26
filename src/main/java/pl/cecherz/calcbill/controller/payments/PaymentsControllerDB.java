package pl.cecherz.calcbill.controller.payments;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pl.cecherz.calcbill.model.Owner;
import pl.cecherz.calcbill.model.Payments;
import pl.cecherz.calcbill.repositories.PaymentsRepository;
import pl.cecherz.calcbill.utils.MessageBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
@Component("PaymentsControllerDB")
public class PaymentsControllerDB {
    /* Wprowadzenie identyfikacji klasy dla narzędzia MessageBuilder */
    private MessageBuilder message = new MessageBuilder(PaymentsControllerDB.class);

    /* Dane zapisane z użyciem obiektu Payments repository trafiają do bazy danych, do pamięci trwałej */
    private final PaymentsRepository paymentsRepository;

    public PaymentsControllerDB(PaymentsRepository paymentsRepository) {
        this.paymentsRepository = paymentsRepository;
    }

    @GetMapping("/select")
    public Iterable<Payments> selectAllPayments() {
        message.getInfo(
                "selectAllPayments()",
                paymentsRepository.findAll()
        );
        return paymentsRepository.findAll();
    }
    /* Dane w formie JSON-a zostają pobrane z bazy danych:
    odpowiednik zapytania SELECT * FROM owners WHERE payments.id = ?;

    Gdy odwołamy się do rekordu, który nie istneje w tabeli metoda rzuci wyjątek: NullPointerException
    Zwracany jest obiekt nie jest lista gdyż id jest unikatowe */
    @GetMapping("/select/id/{id}")
    public Payments findPaymentById(@PathVariable Integer id) {
        if (paymentsRepository.findPaymentById(id) == null) {
            throw new NullPointerException();
        }
        message.getInfo(
                "findPaymentById()",
                paymentsRepository.findPaymentById(id)
        );
        return paymentsRepository.findPaymentById(id);
    }
    /* Dane w formie JSON-a zostają pobrane z bazy danych:
    odpowiednik zapytania SELECT * FROM payments WHERE payments.name = ?;
    Zwracana jest lista, imiona mogą się powtarzać. */
    @GetMapping("/select/kind/{kind}")
    public Iterable<Payments> findPaymentsByKind(@PathVariable String kind) {
        if (paymentsRepository.findPaymentsByKind(kind) == null) {
            throw new NullPointerException();
        }
        message.getInfo(
                "findPaymentsByKind()",
                paymentsRepository.findPaymentsByKind(kind)
        );
        return paymentsRepository.findPaymentsByKind(kind);
    }
    /* Dane w formie JSON-a zostają pobrane z bazy danych:
    odpowiednik zapytania SELECT * FROM payments WHERE payments.id = ? and payments.amount < ?;
    Zwracana jest lista płatności z zakresu */
    @GetMapping("/select/id/{id}/range/{range}")
    public Optional<Payments> filterPaymentsByAmount(@PathVariable Owner id, @PathVariable Double range) {
          Optional<Payments> paymentsList = paymentsRepository.findPaymentsByOwnerId(id)
                .stream()
                .filter(payments -> payments.getAmount() <= range)
                .findAny();
          message.getInfo("filterPaymentsByAmount", paymentsList);
          return paymentsList;
    }
}
