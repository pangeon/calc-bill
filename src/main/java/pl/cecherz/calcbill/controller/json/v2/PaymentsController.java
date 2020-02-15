package pl.cecherz.calcbill.controller.json.v2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pl.cecherz.calcbill.model.json.Payments;
import pl.cecherz.calcbill.utils.JsonDataManager;
import pl.cecherz.calcbill.utils.MessageBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/payments")
@Component("PaymentsControllerJSON-V2")
public class PaymentsController extends HTTPHeaderUtils {
    /* Wprowadzenie identyfikacji klasy dla narzędzia MessageBuilder */
    private MessageBuilder message = new MessageBuilder(pl.cecherz.calcbill.controller.json.v2.PaymentsController.class);

    /* Dane zapisane w postaci listy przechowywane są tylko w pamięci tymczasowej */
    private List<Payments> paymentsList = JsonDataManager.initPayments();

    /* Zwraca JSON-a reprezentację kolekcji z obiektami płatności.
    Przechowuje informacje w cachu przez 5 minut */
    @GetMapping()
    public ResponseEntity<List<Payments>> getAllPayments() {
        message.getInfo("V2 :: getAllPayments()", paymentsList);
        return ResponseEntity.ok().header("Cache-Control", "max-age" + "=300").body(paymentsList);
    }
    /* Zwraca JSON-a reprezentację jednego obiektu płatności z kolekcji. */
    @GetMapping(params = "id")
    public ResponseEntity<Optional<Payments>> getPayment(@RequestParam("id") Integer id) {
        var filteredPayments = findPaymentById(id);
        if (findPaymentById(id).isEmpty()) {
            message.getInfo("V2 :: getPayment()", filteredPayments, "status", HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            message.getInfo("V2 :: getPayment()", filteredPayments, "status", HttpStatus.OK);
            return ResponseEntity.ok().body(filteredPayments);
        }
    }
    /* Metody odpowiedzialne za filtrowanie danych
    Zwraca kod 200 - OK lub kod 404 - NOT FOUND */
    @GetMapping(params = "kind")
    public ResponseEntity<List<Payments>> filterPaymentsByKind(@RequestParam("kind") String kind) {
        var payments = findPaymentsByKind(kind);
        if(payments.isEmpty()) {
            message.getInfo("V2 :: filterPaymentsByKind()", payments, "status", HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            message.getInfo("V2 :: filterPaymentsByKind()", payments, "status", HttpStatus.OK);
            return ResponseEntity.ok().body(payments);
        }
    }
    @GetMapping(params = {"owner_id", "kind"})
    public ResponseEntity<List<Payments>> filterPaymentsByKindAndOwnerId(@RequestParam("owner_id") Integer owner_id, @RequestParam("kind") String kind) {
        var payments = findPaymentsByKindAndOwnerId(owner_id, kind);
        if (payments.isEmpty()) {
            message.getInfo("V2 ::filterPaymentsByKindAndOwnerId()", payments, "status", HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            message.getInfo("V2 ::filterPaymentsByKindAndOwnerId()", payments, "status", HttpStatus.OK);
            return ResponseEntity.ok().body(payments);
        }
    }
    @GetMapping(params = {"owner_id", "min", "max"})
    public ResponseEntity<List<Payments>> filterPaymentsByOwnerIdAndAmonutRange(@RequestParam("owner_id") Integer owner_id, @RequestParam("min") Double min, @RequestParam("max") Double max) {
        var payments = findPaymentsByOwnerIdAndRange(owner_id, min, max);
        if (payments.isEmpty()) {
            message.getInfo("V2 :: filterPaymentsByOwnerIdAndAmonutRange()", payments, "status", HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            message.getInfo("V2 :: filterPaymentsByOwnerIdAndAmonutRange()", payments, "status", HttpStatus.OK);
            return ResponseEntity.ok().body(payments);
        }
    }
    /* --------------------------------------------- */
    /* Zastępuje wartości obiektu Payment
    Zwraca kod 200 - OK lub kod 404 - NOT FOUND */
        @PutMapping("/{id}")
        public ResponseEntity<?> replacePayment(@PathVariable Integer id, @RequestBody Payments newPayment) {
            if (findPaymentById(id).isEmpty()) {
                message.getInfo("V2 :: replacePayment()", "status", HttpStatus.NOT_FOUND);
                return ResponseEntity.notFound().build();
            } else {
                findPaymentById(id).ifPresent(payment -> {
                    payment.setId(newPayment.getId());
                    payment.setOwnerId(newPayment.getOwnerId());
                    payment.setKind(newPayment.getKind());
                    payment.setAmount(newPayment.getAmount());
                    payment.setDate(newPayment.getDate());
                    message.getInfo("V2 :: replacePayment()", Collections.singletonList(payment), newPayment);
                });
                message.getInfo("V2 :: replacePayment()", "status", HttpStatus.OK);
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        }
        @PatchMapping("/{id}")
        public ResponseEntity<?> updatePayment(@PathVariable Integer id, @RequestBody Payments paymentValuesToReplace) {
            if (findPaymentById(id).isEmpty()) {
                message.getInfo("V2 :: updatePayment()", "status", HttpStatus.NOT_FOUND);
                return ResponseEntity.notFound().build();
            } else {
                findPaymentById(id).ifPresent(payment -> {
                    if(paymentValuesToReplace.getId() != null) payment.setId(paymentValuesToReplace.getId());
                    if(paymentValuesToReplace.getOwnerId() != null) payment.setOwnerId(paymentValuesToReplace.getOwnerId());
                    if(paymentValuesToReplace.getKind() != null) payment.setKind(paymentValuesToReplace.getKind());
                    if(paymentValuesToReplace.getAmount() != null) payment.setAmount(paymentValuesToReplace.getAmount());
                    if(paymentValuesToReplace.getDate() != null) payment.setDate(paymentValuesToReplace.getDate());
                    message.getInfo("V2 :: updatePayment()", Collections.singletonList(payment), paymentValuesToReplace);
                });
                message.getInfo("V2 :: updatePayment()", "status", HttpStatus.OK);
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        }
    /* --------------------------------------------- */
    /* Dodaje płatność do kolekcji { "id": 3, "owner_id": {}, "kind": "sex", amount": 20.0, "date": null }
    Zwaraca kod 201 - CREATED
    */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public void addPayment(@RequestBody Payments payment) {
        message.getInfo("V2 :: addPayment()", payment, HttpStatus.CREATED);
        paymentsList.add(payment);
    }
    /* Usuwa płatność z kolekcji po wskazanym id
    Zwaraca kod 200 - OK lub kod 404 - NOT FOUND */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Integer id) {
        message.getInfo("V2 :: deletePayment()", "id: " + id);
        if(paymentsList.removeIf(payment -> payment.getId().equals(id))) {
            message.getInfo("V2 :: deletePayment()", "status: " + HttpStatus.OK);
            return ResponseEntity.ok().build();
        } else {
            message.getInfo("V2 :: deletePayment()", "status: " + HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }
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
