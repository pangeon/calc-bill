package pl.cecherz.calcbill.controller.json.v2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pl.cecherz.calcbill.model.json.Payments;
import pl.cecherz.calcbill.utils.JsonDataManager;
import pl.cecherz.calcbill.utils.MessageBuilder;

import java.util.List;

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
}
