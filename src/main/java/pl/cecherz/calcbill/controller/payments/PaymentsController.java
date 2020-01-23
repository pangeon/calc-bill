package pl.cecherz.calcbill.controller.payments;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.cecherz.calcbill.model.Payments;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/api/payments")
@Component("PaymentsController")
public class PaymentsController {
    private Collection<Payments> payments = new ArrayList<>();

    @GetMapping()
    public Iterable<Payments> getPayments() {
        return payments;
    }
    /*
    {
        "id": 3,
        "owner_id": {},
        "kind": "sex",
        "amount": 20.0,
        "date": null
    }
     */
    @PostMapping()
    public void addPayment(@RequestBody Payments payment) {
        payments.add(payment);
    }
}
