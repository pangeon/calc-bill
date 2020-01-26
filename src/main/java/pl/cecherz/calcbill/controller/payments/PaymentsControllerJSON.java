package pl.cecherz.calcbill.controller.payments;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.cecherz.calcbill.controller.owner.OwnerControllerJSON;
import pl.cecherz.calcbill.model.Payments;
import pl.cecherz.calcbill.utils.MessageBuilder;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/api/payments")
@Component("PaymentsControllerJSON")
public class PaymentsControllerJSON {
    /* Wprowadzenie identyfikacji klasy dla narzędzia MessageBuilder */
    private MessageBuilder message = new MessageBuilder(PaymentsControllerJSON.class);

    /* Dane zapisane w postaci listy przechowywane są tylko w pamięci tymczasowej */
    private Collection<Payments> payments = new ArrayList<>();
    {
        message.getInfo("Init payments collection:",payments);
    }

    /* Zwraca JSON-a reprezentację kolekcji z obiektami płatności. */
    @GetMapping()
    public Iterable<Payments> getPayments() {
        message.getInfo("getPayments()", payments);
        return payments;
    }
    /* Dodaje płatność do kolekcji { "id": 3, "owner_id": {}, "kind": "sex", amount": 20.0, "date": null } */
    @PostMapping()
    public void addPayment(@RequestBody Payments payment) {
        message.getInfo("addPayment()", payment);
        payments.add(payment);
    }
}
