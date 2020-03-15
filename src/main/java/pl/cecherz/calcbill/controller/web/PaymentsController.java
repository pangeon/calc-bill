package pl.cecherz.calcbill.controller.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.cecherz.calcbill.controller.web.ui_utils.WebViewBuilder;
import pl.cecherz.calcbill.exeptions.DataIntegrityException;
import pl.cecherz.calcbill.exeptions.EmptyFindResultException;
import pl.cecherz.calcbill.exeptions.EntityNotFoundException;
import pl.cecherz.calcbill.exeptions.RestExceptionHandler;
import pl.cecherz.calcbill.model.db.Owner;
import pl.cecherz.calcbill.model.db.Payments;
import pl.cecherz.calcbill.repositories.PaymentsRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Klasa ma zastosowanie w przypadku odwoływania się do całego zbioru płatność bez uwzględnienia podziału na posiadaczy.
 *
 * Metody
 * --------
 * getAllPayments()
 * filterPaymentsByKind(? kind)
 * filterPaymentsByOwnerIdAndAmonutRang(? min max)
 *
 * addPayment({id}) -- tworzy relacje na poziomie bazy danych
 * updatePayment({id})
 * deletePayment({id})
 */

@RestController
@RequestMapping("/api/web/payments")
@Component("PaymentsControllerWeb")
public class PaymentsController extends RestExceptionHandler {
    private final PaymentsRepository paymentsRepository;

    public PaymentsController(PaymentsRepository paymentsRepository) {
        this.paymentsRepository = paymentsRepository;
    }
    @GetMapping()
    public ModelAndView getAllPayments() {
        return WebViewBuilder.returnPaymentListView(paymentsRepository);
    }
    @GetMapping("/{id}")
    public Payments getPayment(
            @PathVariable Integer id) {
        Payments payment = paymentsRepository.findPaymentById(id);
        if (payment == null) throw new EntityNotFoundException(id);
        return payment;
    }
    @GetMapping(params = "kind")
    public ModelAndView filterPaymentsByKind(@RequestParam("kind") String kind) {
        List<Payments> paymentsByKind = paymentsRepository.findPaymentsByKind(kind);;
        if (paymentsByKind.isEmpty()) throw new EmptyFindResultException(kind);

        return WebViewBuilder.returnPaymentListView(paymentsRepository, paymentsByKind);
    }
    @GetMapping(params = {"min", "max"})
    public ModelAndView filterPaymentsByOwnerIdAndAmonutRange(@RequestParam("min") Double min, @RequestParam("max") Double max) {
        List<Payments> paymentsByAmount = paymentsRepository.findPaymentsByAmountBetween(min, max);
        String range = min + " - " + max;
        if (paymentsByAmount.isEmpty()) throw new EmptyFindResultException(range);

        return WebViewBuilder.returnPaymentListView(paymentsRepository, paymentsByAmount);
    }
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPayment (@RequestBody Payments body, @PathVariable Integer id) {
        Owner owner = new Owner();
        owner.setId(id);
        body.setOwnerId(owner);
        body.setDate(new Timestamp(System.currentTimeMillis()));
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
        paymentsValuesToReplace.ifPresent(payment -> {
            payment.setId(id);
            payment.setAmount(newPayment.getAmount());
            payment.setKind(newPayment.getKind());
            payment.setDate(new Timestamp(System.currentTimeMillis()));
        });
        paymentsRepository.save(paymentsValuesToReplace.orElse(null));
    }
    @PatchMapping("/{id}")
    public void updatePayment(@PathVariable Integer id, @RequestBody Payments paymentToUpdate) {
        Optional<Payments> paymentValuesToUpdate = paymentsRepository.findById(id);
        if(paymentValuesToUpdate.orElse(null) == null) throw new EntityNotFoundException(id);
        paymentValuesToUpdate.ifPresent(payment -> {
            if(paymentToUpdate.getId() != null) payment.setId(id);
            if(paymentToUpdate.getAmount() != null) payment.setAmount(paymentToUpdate.getAmount());
            if(paymentToUpdate.getKind() != null) payment.setKind(paymentToUpdate.getKind());
            payment.setDate(new Timestamp(System.currentTimeMillis()));
        });
        paymentsRepository.save(paymentValuesToUpdate.orElse(null));
    }
    @DeleteMapping("/{id}")
    void deletePayment(@PathVariable Integer id) {
        Payments paymentToDelete = paymentsRepository.findPaymentById(id);
        if(paymentToDelete == null) throw new EntityNotFoundException(id);
        paymentsRepository.delete(paymentToDelete);
    }
}
