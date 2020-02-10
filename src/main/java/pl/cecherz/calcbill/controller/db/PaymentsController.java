package pl.cecherz.calcbill.controller.db;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.cecherz.calcbill.model.db.Owner;
import pl.cecherz.calcbill.model.db.Payments;
import pl.cecherz.calcbill.repositories.PaymentsRepository;
import pl.cecherz.calcbill.utils.MessageBuilder;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/db/payments")
@Component("PaymentsControllerDB")
public class PaymentsController {
    /* Wprowadzenie identyfikacji klasy dla narzędzia MessageBuilder */
    private MessageBuilder message = new MessageBuilder(PaymentsController.class);

    /* Dane zapisane z użyciem obiektu Payments repository trafiają do bazy danych, do pamięci trwałej */
    private final PaymentsRepository paymentsRepository;

    public PaymentsController(PaymentsRepository paymentsRepository) {
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
          message.getInfo("filterPaymentsByAmount()", paymentsList);
          return paymentsList;
    }
    /* Dane w formie JSON-a zostają pobrane z bazy danych:
    odpowiednik zapytania SELECT * FROM payments WHERE payments.amount > ? and payments.amount < ?;
    Zwracana jest lista płatności z zakresu */
    @GetMapping("/select/range/min/{min}/max/{max}")
    public List<Payments> filterPaymentsByAmountRange(@PathVariable Double min, @PathVariable Double max) {
        List<Payments> paymentsList = paymentsRepository.findPaymentsByAmountBetween(min, max);
        message.getInfo("filterPaymentsByAmountRange()", paymentsList);
        return paymentsList;
    }
    /* Dane w formie JSON-a zostają pobrane z bazy danych:
    odpowiednik zapytania: SELECT * FROM payments WHERE payments.id = ? and payments.amount > ? and payments.amount < ?;
    Zwracana jest lista płatności z zakresu */
    @GetMapping("/select/id/{id}/range/min/{min}/max/{max}")
    public List<Payments> filterPaymentsByAmountRangeAndOwnerId(@PathVariable Owner id,
            @PathVariable Double min, @PathVariable Double max) {
        List<Payments> paymentsList = paymentsRepository.findPaymentsByOwnerIdAndAmountBetween(id, min, max);
        message.getInfo("filterPaymentsByAmountRangeAndOwnerId()", paymentsList);
        return paymentsList;
    }
    /* Dane w formie JSON-a zostają zapisane w bazie danych:
    opowiednik zapytania INSERT INTO payments (id, amount, kind) VALUES (...)
    Podanie id jest koniecznie mimo iż model zawiera właściwość @GeneratedValue(strategy = GenerationType.IDENTITY)
    - Dodaje najwyższe możliwe id większe o jeden od ostatniego
    - Napisuje wartość gdy podamy istniejące id

    PROBLEM: metoda nie wstawia klucza obcego, płatność nie ma posiada, nie istnieje relacja pomiędzy danymi,
    metoda działa tylko po zdjęciu ograniczenia z bazy danych Payments [ownerId nullable = true]
    */
    @PostMapping("/insert/{id}")
    void insertOwner(@RequestBody Payments body, @PathVariable Integer id) {
        message.getInfo("insertPayment()", body);
        Owner owner = new Owner();
        owner.setId(id);
        body.setOwnerId(owner);
        body.setDate(new Timestamp(System.currentTimeMillis()));
        paymentsRepository.save(body);
    }
}
