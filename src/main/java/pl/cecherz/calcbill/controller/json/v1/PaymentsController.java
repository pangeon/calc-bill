package pl.cecherz.calcbill.controller.json.v1;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.cecherz.calcbill.model.json.Payments;
import pl.cecherz.calcbill.utils.JsonDataManager;
import pl.cecherz.calcbill.utils.MessageBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payments")
@Component("PaymentsControllerJSON-V1")
public class PaymentsController {
    /* Wprowadzenie identyfikacji klasy dla narzędzia MessageBuilder */
    private MessageBuilder message = new MessageBuilder(PaymentsController.class);

    /* Dane zapisane w postaci listy przechowywane są tylko w pamięci tymczasowej */
    private Collection<Payments> paymentsList = JsonDataManager.initPayments();

    /* Zwraca JSON-a reprezentację kolekcji z obiektami płatności. */
    @GetMapping()
    public Iterable<Payments> getAllPayments() {
        message.getInfo("getAllPayments()", paymentsList);
        return paymentsList;
    }
    /* Zwraca JSON-a reprezentację jednego obiektu płatności z kolekcji. */
    @GetMapping(params = "id")
    public Optional<Payments> getPayment(@RequestParam("id") Integer id) {
        Optional<Payments> filteredPayments = findPaymentById(id);
        message.getInfo("getPayment()", filteredPayments);
        return filteredPayments;
    }
    /* Metody odpowiedzialne za filtrowanie danych */
        @GetMapping(params = "kind")
        public Optional<List<Payments>> filterPaymentsByKind(@RequestParam("kind") String kind) {
            Optional<List<Payments>> payments = Optional.ofNullable(findPaymentsByKind(kind));
            message.getInfo("filterPaymentsByKind()", payments);
            return payments;
        }
        @GetMapping(params = {"owner_id", "kind"})
        public Optional<List<Payments>> filterPaymentsByKindAndOwnerId(
                @RequestParam("owner_id") Integer owner_id, @RequestParam("kind") String kind) {
            Optional<List<Payments>> payments = Optional.ofNullable(findPaymentsByKindAndOwnerId(owner_id, kind));
            message.getInfo("filterPaymentsByKindAndOwnerId()", payments);
            return payments;
        }
        @GetMapping(params = {"owner_id", "min", "max"})
        public Optional<List<Payments>> filterPaymentsByOwnerIdAndAmonutRange(
                @RequestParam("owner_id") Integer owner_id, @RequestParam("min") Double min, @RequestParam("max") Double max) {
            Optional<List<Payments>> filteredPayments = Optional.ofNullable(findPaymentsByOwnerIdAndRange(owner_id, min, max));
            message.getInfo("filterPaymentsByOwnerIdAndAmonutRange()", filteredPayments);
            return filteredPayments;
        }
    /* --------------------------------------------- */

    /* Zastępuje wartości obiektu Payment */
        @PutMapping("/{id}")
        public void replacePayment(@PathVariable Integer id, @RequestBody Payments newPayment) {
            findPaymentById(id).ifPresent(payment -> {
                payment.setId(newPayment.getId());
                payment.setOwnerId(newPayment.getOwnerId());
                payment.setKind(newPayment.getKind());
                payment.setAmount(newPayment.getAmount());
                payment.setDate(newPayment.getDate());
                message.getInfo("replacePayment()", Collections.singletonList(payment), newPayment);
            });
        }
        @PatchMapping("/{id}")
        public void updatePayment(@PathVariable Integer id, @RequestBody Payments paymentValuesToReplace) {
            findPaymentById(id).ifPresent(payment -> {
                if(paymentValuesToReplace.getId() != null) payment.setId(paymentValuesToReplace.getId());
                if(paymentValuesToReplace.getOwnerId() != null) payment.setOwnerId(paymentValuesToReplace.getOwnerId());
                if(paymentValuesToReplace.getKind() != null) payment.setKind(paymentValuesToReplace.getKind());
                if(paymentValuesToReplace.getAmount() != null) payment.setAmount(paymentValuesToReplace.getAmount());
                if(paymentValuesToReplace.getDate() != null) payment.setDate(paymentValuesToReplace.getDate());
                message.getInfo("updatePayment()", Collections.singletonList(payment), paymentValuesToReplace);
            });
        }
    /* --------------------------------------------- */

    /* Dodaje płatność do kolekcji { "id": 3, "owner_id": {}, "kind": "sex", amount": 20.0, "date": null } */
    @PostMapping()
    public void addPayment(@RequestBody Payments payment) {
        message.getInfo("addPayment()", payment);
        paymentsList.add(payment);
    }
    /* Usuwa płatność z kolekcji po wskazanym id */
    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable Integer id) {
        paymentsList.removeIf(payment -> payment.getId().equals(id));
    }
    /* Metody prywatne wykorzystywane w API */
        private Optional<Payments> findPaymentById(Integer id) {
            return paymentsList.stream()
                    .filter(payment -> payment.getId().equals(id))
                    .findAny();
        }
        private List<Payments> findPaymentsByOwnerId(Integer owner_id) {
            return paymentsList.stream()
                    .filter(payment -> payment.getOwnerId().equals(owner_id)).collect(Collectors.toList());
        }
        private List<Payments> findPaymentsByKind(String kind) {
            return paymentsList.stream()
                    .filter(payment -> payment.getKind().equals(kind)).collect(Collectors.toList());
        }
        private List<Payments> findPaymentsByKindAndOwnerId(Integer owner_id, String kind) {
            return findPaymentsByOwnerId(owner_id).stream()
                    .filter(payment -> payment.getKind().equals(kind))
                    .collect(Collectors.toList());
        }
        private List<Payments> findPaymentsByOwnerIdAndRange(Integer owner_id, Double min, Double max) {
            return findPaymentsByOwnerId(owner_id).stream()
                    .filter(payment -> payment.getAmount() > min && payment.getAmount() < max)
                    .collect(Collectors.toList());
        }
    /* --------------------------------------------- */
}
