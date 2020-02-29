package pl.cecherz.calcbill.controller.db;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pl.cecherz.calcbill.model.db.Owner;
import pl.cecherz.calcbill.model.db.Payments;
import pl.cecherz.calcbill.repositories.PaymentsRepository;
import pl.cecherz.calcbill.utils.MessageBuilder;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/db/payments")
@Component("PaymentsControllerDB")
public class PaymentsController {
    private MessageBuilder message = new MessageBuilder(PaymentsController.class);
    private final PaymentsRepository paymentsRepository;

    public PaymentsController(PaymentsRepository paymentsRepository) {
        this.paymentsRepository = paymentsRepository;
    }
    @GetMapping()
    public Iterable<Payments> getAllPayments() {
        message.getInfo("getAllPayments()", paymentsRepository.findAll());
        return paymentsRepository.findAll();
    }
    /* Metody zawsze zwracają jeden obiekt gdyż nie widzą relacji */
    @GetMapping("/{id}")
    public Payments getPayment(@PathVariable Integer id) {
        if (paymentsRepository.findPaymentById(id) == null) {
            throw new NullPointerException();
        }
        message.getInfo("getPayment()", paymentsRepository.findPaymentById(id));
        return paymentsRepository.findPaymentById(id);
    }
    @GetMapping(params = "kind")
    public Iterable<Payments> filterPaymentsByKind(@RequestParam("kind") String kind) {
        if (paymentsRepository.findPaymentsByKind(kind) == null) {
            throw new NullPointerException();
        }
        message.getInfo("filterPaymentsByKind()", paymentsRepository.findPaymentsByKind(kind));
        return paymentsRepository.findPaymentsByKind(kind);
    }
    @GetMapping(params = {"min", "max"})
    public List<Payments> filterPaymentsByOwnerIdAndAmonutRange(
            @RequestParam("min") Double min,
            @RequestParam("max") Double max) {
        List<Payments> paymentsList = paymentsRepository.findPaymentsByAmountBetween(min, max);
        message.getInfo("filterPaymentsByOwnerIdAndAmonutRange()", paymentsList);
        return paymentsList;
    }
    @PostMapping("/{id}")
    void addPayment(@RequestBody Payments body, @PathVariable Integer id) {
        message.getInfo("addPayment()", body);
        Owner owner = new Owner();
        owner.setId(id);
        body.setOwnerId(owner);
        body.setDate(new Timestamp(System.currentTimeMillis()));
        paymentsRepository.save(body);
    }
}
