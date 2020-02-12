package pl.cecherz.calcbill.controller.json.v2;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
