package pl.cecherz.calcbill.controller.db;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pl.cecherz.calcbill.exeptions.DataIntegrityException;
import pl.cecherz.calcbill.exeptions.EmptyFindResultException;
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
 *
 * addPayment({id}) -- tworzy relacje na poziomie bazy danych
 * replacePayment({id})
 * updatePayment({id})
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
    public List<Payments> getAllPayments() {
        message.getInfo("getAllPayments()", paymentsRepository.findAll());
        return (List<Payments>) paymentsRepository.findAll();
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
        List<Payments> paymentsByKind = paymentsRepository.findPaymentsByKind(kind);
        message.getInfo("filterPaymentsByKind()", paymentsByKind);

        if (paymentsByKind.isEmpty()) throw new EmptyFindResultException(kind);
        return paymentsByKind;
    }
    @GetMapping(params = {"min", "max"})
    public List<Payments> filterPaymentsByAmonutRange(
            @RequestParam("min") Double min,
            @RequestParam("max") Double max) {
        List<Payments> paymentsByAmount = paymentsRepository.findPaymentsByAmountBetween(min, max);
        message.getInfo("filterPaymentsByOwnerIdAndAmonutRange()", paymentsByAmount);

        String range = min + " - " + max;
        if (paymentsByAmount.isEmpty()) throw new EmptyFindResultException(range);
        return paymentsByAmount;
    }
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPayment (
            @RequestBody Payments body,
            @PathVariable Integer id) {
        Owner owner = new Owner();
        owner.setId(id);
        body.setOwnerId(owner);
        body.setDate(new Timestamp(System.currentTimeMillis()));
        message.getInfo("addPayment()", body, owner);
        try {
            paymentsRepository.save(body);
        } catch (Exception e) {
            throw new DataIntegrityException(id, e.getCause().toString(), e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public void replacePayment(@PathVariable Integer id, @RequestBody Payments newPayment) {
        Optional<Payments> paymentsValuesToReplace = paymentsRepository.findById(id);
        if(paymentsValuesToReplace.orElse(null) == null) throw new EntityNotFoundException(id);
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
        if(paymentValuesToUpdate.orElse(null) == null) throw new EntityNotFoundException(id);
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
        Payments paymentToDelete = paymentsRepository.findPaymentById(id);
        if(paymentToDelete == null) throw new EntityNotFoundException(id);
        message.getInfo("deletePayment()", paymentToDelete);
        paymentsRepository.delete(paymentToDelete);
    }
}
