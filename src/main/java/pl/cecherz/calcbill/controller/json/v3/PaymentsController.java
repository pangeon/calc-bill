package pl.cecherz.calcbill.controller.json.v3;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pl.cecherz.calcbill.controller.json.rest_utils.HTTPHeaderUtils;
import pl.cecherz.calcbill.controller.json.rest_utils.HateoasBuilder;
import pl.cecherz.calcbill.model.json.Payments;
import pl.cecherz.calcbill.utils.JsonDataManager;
import pl.cecherz.calcbill.utils.MessageBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.cecherz.calcbill.controller.json.rest_utils.HateoasBuilder.*;

@RestController
@RequestMapping("/api/v3/payments")
@Component("PaymentsControllerJSON-V3")
public class PaymentsController extends HTTPHeaderUtils {
    /* Wprowadzenie identyfikacji klasy dla narzędzia MessageBuilder */
    private MessageBuilder message = new MessageBuilder(PaymentsController.class);

    /* Dane zapisane w postaci listy przechowywane są tylko w pamięci tymczasowej */
    private List<Payments> paymentsList = JsonDataManager.initPayments();

    /* Zwraca JSON-a reprezentację kolekcji z obiektami płatności.
    Przechowuje informacje w cachu przez 5 minut */
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Payments>>> getAllPayments() {
        var collectionModel = new CollectionModel<>(
                paymentsList.stream().map(HateoasBuilder::mapToEnityModel)
                        .collect(Collectors.toList()));
        linkToPaymentSelf(collectionModel, "self");
        linkToPaymentId(collectionModel, "PaymentById", null);
        queryPaymentKind(collectionModel, "PaymentByKind", null);
        queryPaymentKindAndOwner(collectionModel, "PaymentsByOwnerIdAndKind", null, null);
        queryPaymentOwnerAndAmountRange(collectionModel, "PaymentsByOwnerIdAndAmonutRange", null, null, null);
        message.getInfo("V3 :: getAllPayments()", collectionModel);
        return ResponseEntity.ok().header("Cache-Control", "max-age" + "=300")
                .body(collectionModel);
    }
    /* Zwraca JSON-a reprezentację jednego obiektu płatności z kolekcji. */
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Payments>> getPayment(@PathVariable("id") Integer id) {
        var filteredPayments = findPaymentById(id);
        if (findPaymentById(id).isEmpty()) {
            message.getInfo("V3 :: getPayment()", filteredPayments, "status", HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            message.getInfo("V3 :: getPayment()", filteredPayments, "status", HttpStatus.OK);
            return ResponseEntity.ok().body(filteredPayments.map(HateoasBuilder::mapToEnityModel).orElse(null));
        }
    }
    /* Metody odpowiedzialne za filtrowanie danych
    Zwraca kod 200 - OK lub kod 404 - NOT FOUND */
    @GetMapping(params = "kind")
    public ResponseEntity<CollectionModel<EntityModel<Payments>>> filterPaymentsByKind(@RequestParam("kind") String kind) {
        var checkPayments = findPaymentsByKind(kind);
        var collectionModel = new CollectionModel<>(
                paymentsList.stream().filter(
                        payments -> kind.equals(payments.getKind())
                ).map(HateoasBuilder::mapToEnityModel).collect(Collectors.toList())
        );
        linkToPaymentSelf(collectionModel, "allPayments");
        queryPaymentKind(collectionModel, "self", kind);
        if(checkPayments.isEmpty()) {
            message.getInfo("V3 :: filterPaymentsByKind()", collectionModel, "status", HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        } else {
            message.getInfo("V3 :: filterPaymentsByKind()", collectionModel, "status", HttpStatus.OK);
            return ResponseEntity.ok().body(collectionModel);
        }
    }
    @GetMapping(params = {"owner_id", "kind"})
    public ResponseEntity<CollectionModel<EntityModel<Payments>>> filterPaymentsByKindAndOwnerId(@RequestParam("owner_id") Integer owner_id, @RequestParam("kind") String kind) {
        var checkPayments = findPaymentsByKindAndOwnerId(owner_id, kind);
        var collectionModel = new CollectionModel<>(
                paymentsList.stream().filter(
                        payments -> kind.equals(payments.getKind()) && (owner_id.equals(payments.getOwnerId()))
                ).map(HateoasBuilder::mapToEnityModel).collect(Collectors.toList())
        );
        linkToPaymentSelf(collectionModel, "allPayments");
        queryPaymentKindAndOwner(collectionModel, "self", owner_id, kind);
        if (checkPayments.isEmpty()) {
            message.getInfo("V3 ::filterPaymentsByKindAndOwnerId()", checkPayments, "status", HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            message.getInfo("V3 ::filterPaymentsByKindAndOwnerId()", checkPayments, "status", HttpStatus.OK);
            return ResponseEntity.ok().body(collectionModel);
        }
    }
    @GetMapping(params = {"owner_id", "min", "max"})
    public ResponseEntity<CollectionModel<EntityModel<Payments>>> filterPaymentsByOwnerIdAndAmonutRange(@RequestParam("owner_id") Integer owner_id, @RequestParam("min") Double min, @RequestParam("max") Double max) {
        var checkPayments = findPaymentsByOwnerIdAndRange(owner_id, min, max);
        var collectionModel = new CollectionModel<>(
                paymentsList.stream().filter(
                        payments -> owner_id.equals(payments.getOwnerId()) && (min < payments.getAmount()) && (max > payments.getAmount())
                ).map(HateoasBuilder::mapToEnityModel).collect(Collectors.toList())
        );
        linkToPaymentSelf(collectionModel, "allPayments");
        queryPaymentOwnerAndAmountRange(collectionModel, "self", owner_id, min, max);
        if (checkPayments.isEmpty()) {
            message.getInfo("V3 :: filterPaymentsByOwnerIdAndAmonutRange()", checkPayments, "status", HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            message.getInfo("V3 :: filterPaymentsByOwnerIdAndAmonutRange()", checkPayments, "status", HttpStatus.OK);
            return ResponseEntity.ok().body(collectionModel);
        }
    }
    /* --------------------------------------------- */
    /* Zastępuje wartości obiektu Payment
    Zwraca kod 200 - OK lub kod 404 - NOT FOUND */
        @PutMapping("/{id}")
        public ResponseEntity<?> replacePayment(@PathVariable Integer id, @RequestBody Payments newPayment) {
            if (findPaymentById(id).isEmpty()) {
                message.getInfo("V3 :: replacePayment()", "status", HttpStatus.NOT_FOUND);
                return ResponseEntity.notFound().build();
            } else {
                findPaymentById(id).ifPresent(payment -> {
                    payment.setId(newPayment.getId());
                    payment.setOwnerId(newPayment.getOwnerId());
                    payment.setKind(newPayment.getKind());
                    payment.setAmount(newPayment.getAmount());
                    payment.setDate(newPayment.getDate());
                    message.getInfo("V3 :: replacePayment()", Collections.singletonList(payment), newPayment);
                });
                message.getInfo("V3 :: replacePayment()", "status", HttpStatus.OK);
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        }
        @PatchMapping("/{id}")
        public ResponseEntity<?> updatePayment(@PathVariable Integer id, @RequestBody Payments paymentValuesToReplace) {
            if (findPaymentById(id).isEmpty()) {
                message.getInfo("V3 :: updatePayment()", "status", HttpStatus.NOT_FOUND);
                return ResponseEntity.notFound().build();
            } else {
                findPaymentById(id).ifPresent(payment -> {
                    if(paymentValuesToReplace.getId() != null) payment.setId(paymentValuesToReplace.getId());
                    if(paymentValuesToReplace.getOwnerId() != null) payment.setOwnerId(paymentValuesToReplace.getOwnerId());
                    if(paymentValuesToReplace.getKind() != null) payment.setKind(paymentValuesToReplace.getKind());
                    if(paymentValuesToReplace.getAmount() != null) payment.setAmount(paymentValuesToReplace.getAmount());
                    if(paymentValuesToReplace.getDate() != null) payment.setDate(paymentValuesToReplace.getDate());
                    message.getInfo("V3 :: updatePayment()", Collections.singletonList(payment), paymentValuesToReplace);
                });
                message.getInfo("V3 :: updatePayment()", "status", HttpStatus.OK);
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
        message.getInfo("V3 :: addPayment()", payment, HttpStatus.CREATED);
        paymentsList.add(payment);
    }
    /* Usuwa płatność z kolekcji po wskazanym id
    Zwaraca kod 200 - OK lub kod 404 - NOT FOUND */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Integer id) {
        message.getInfo("V3 :: deletePayment()", "id: " + id);
        if(paymentsList.removeIf(payment -> payment.getId().equals(id))) {
            message.getInfo("V2 :: deletePayment()", "status: " + HttpStatus.OK);
            return ResponseEntity.ok().build();
        } else {
            message.getInfo("V3 :: deletePayment()", "status: " + HttpStatus.NOT_FOUND);
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
