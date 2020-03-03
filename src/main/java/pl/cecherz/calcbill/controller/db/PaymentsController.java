package pl.cecherz.calcbill.controller.db;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pl.cecherz.calcbill.exeptions.EntityNotFoundException;
import pl.cecherz.calcbill.exeptions.RestExceptionHandler;
import pl.cecherz.calcbill.model.db.Owner;
import pl.cecherz.calcbill.model.db.Payments;
import pl.cecherz.calcbill.repositories.PaymentsRepository;
import pl.cecherz.calcbill.utils.MessageBuilder;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Klasa ma zastosowanie w przypadku odwoływania się do całego zbioru płatność bez uwzględnienia podziału na posiadaczy.
 *
 * Metody
 * --------
 * getAllPayments()
 * getPayment({id})
 * filterPaymentsByKind(? kind)
 * filterPaymentsByOwnerIdAndAmonutRang(? min max)
 * addPayment({id}) -- tworzy relacje na poziomie bazy danych
 * deletePayment({id})
 */

@RestController
@RequestMapping("/api/db/payments")
@Component("PaymentsControllerDB")
public class PaymentsController extends RestExceptionHandler {
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
    @GetMapping("/{id}")
    public Payments getPayment(
            @PathVariable Integer id) {
        Payments payment = paymentsRepository.findPaymentById(id);
        if (payment == null) throw new EntityNotFoundException(id);
        message.getInfo("getPayment()", payment);
        return payment;
    }
    @GetMapping(params = "kind")
    public List<Payments> filterPaymentsByKind(
            @RequestParam("kind") String kind) {
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
    public void addPayment(
            @RequestBody Payments body,
            @PathVariable Integer id) {
        message.getInfo("addPayment()", body);
        Owner owner = new Owner();
        owner.setId(id);
        body.setOwnerId(owner);
        body.setDate(new Timestamp(System.currentTimeMillis()));
        paymentsRepository.save(body);
    }
    @PutMapping("/{id}")
    public void replacePayment(@PathVariable Integer id, @RequestBody Payments newPayment) {
        Optional<Payments> paymentsValuesToReplace = paymentsRepository.findById(id);
        message.getInfo("start :: replacePayments()", paymentsValuesToReplace);
        paymentsValuesToReplace.ifPresent(payment -> {
            payment.setId(id);
            payment.setAmount(newPayment.getAmount());
            payment.setKind(newPayment.getKind());
            payment.setDate(new Timestamp(System.currentTimeMillis()));
        });
        paymentsRepository.save(paymentsValuesToReplace.orElse(null));
        message.getInfo("end :: replacePayments()", newPayment);
    }
    @PatchMapping("/{id}")
    public void updatePayment(
            @PathVariable Integer id,
            @RequestBody Payments paymentToUpdate) {
        Optional<Payments> paymentValuesToUpdate = paymentsRepository.findById(id);
        message.getInfo("start :: updatePayment()", paymentValuesToUpdate);
        paymentValuesToUpdate.ifPresent(payment -> {
            if(paymentToUpdate.getId() != null) payment.setId(id);
            if(paymentToUpdate.getAmount() != null) payment.setAmount(paymentToUpdate.getAmount());
            if(paymentToUpdate.getKind() != null) payment.setKind(paymentToUpdate.getKind());
            payment.setDate(new Timestamp(System.currentTimeMillis()));
        });
        paymentsRepository.save(paymentValuesToUpdate.orElse(null));
        message.getInfo("end :: updatePayment()", paymentValuesToUpdate);
    }
    @DeleteMapping("/{id}")
    void deletePayment(@PathVariable Integer id) {
        message.getInfo("deletePayment()", paymentsRepository.findPaymentById(id));
        paymentsRepository.delete(paymentsRepository.findPaymentById(id));
    }
}
