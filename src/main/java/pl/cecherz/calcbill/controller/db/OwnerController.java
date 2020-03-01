package pl.cecherz.calcbill.controller.db;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pl.cecherz.calcbill.model.db.Owner;
import pl.cecherz.calcbill.model.db.Payments;
import pl.cecherz.calcbill.repositories.OwnerRepository;
import pl.cecherz.calcbill.utils.MessageBuilder;
import pl.cecherz.calcbill.utils.PaymentsCalculator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa ma zastosowanie w przypadku odwoływania się do całego zbioru płatności z uwzględnienieniem
 * podziału na posiadaczy. Każdy obiekt Owners zawiera w sobie listę danych płatności, które tworzone są by wykorzystaniu
 * klasy PaymentsController. Relacje występują na poziomie dazy danych.
 *
 * Metody
 * --------
 * getAllOwners()
 * getOwner(/{id})
 * getOwnerPayments(/{id}/payments)
 * getOwnerPaymentsByKind(/{id}/payments/{kind})
 * getOwnerPaymentsByAmonutRange(/{id}/payments/{min}/{max})
 * getSumOwnerPayments(/{id}/payments/sum)
 * getSumOwnerPaymentsByKind(/{id}/payments/sum/{kind})
 * addOwner()
 * deleteOwner({id}) - usuwa dane z bazy: tabele Owner i Payments
 */
@RestController
@RequestMapping("/api/db/owners")
@Component("OwnerControllerDB")
public class OwnerController {
    private MessageBuilder message = new MessageBuilder(OwnerController.class);
    private final OwnerRepository ownerRepository;

    public OwnerController(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }
    @GetMapping()
    public Iterable<Owner> getAllOwners() {
        message.getInfo("getAllOwners()", ownerRepository.findAll());
        return ownerRepository.findAll();
    }
    @GetMapping("/{id}")
    public Owner getOwner(
            @PathVariable Integer id) {
        if (ownerRepository.findOwnerById(id) == null) {
            throw new NullPointerException();
        }
        message.getInfo("getOwner()", ownerRepository.findOwnerById(id));
        return ownerRepository.findOwnerById(id);
    }
    @GetMapping("/{id}/payments")
    public List<Payments> getOwnerPayments(
            @PathVariable Integer id) {
        final List<Payments> payments = ownerRepository.findOwnerById(id).getPayments();
        message.getInfo("getOwnerPayments()", payments);
        return payments;
    }
    @GetMapping("/{id}/payments/{kind}")
    public List<Payments> getOwnerPaymentsByKind(
            @PathVariable Integer id,
            @PathVariable String kind) {
        final List<Payments> ownerPaymentsByKind = ownerRepository.findOwnerById(id).getPayments()
                .stream().filter(payment -> payment.getKind().equals(kind))
                .collect(Collectors.toList());
        message.getInfo("getOwnerPaymentsByKind()", ownerPaymentsByKind);
        return ownerPaymentsByKind;
    }
    @GetMapping("/{id}/payments/{min}/{max}")
    public List<Payments> getOwnerPaymentsByAmonutRange(
            @PathVariable Integer id,
            @PathVariable Double min,
            @PathVariable Double max) {
        final List<Payments> ownerPaymentsByKind = ownerRepository.findOwnerById(id).getPayments()
                .stream().filter(payment -> payment.getAmount() > min && payment.getAmount() < max)
                .collect(Collectors.toList());
        message.getInfo("getOwnerPaymentsByAmonutRange()", ownerPaymentsByKind);
        return ownerPaymentsByKind;
    }
    @GetMapping(value = "/{id}/payments/sum", produces = MediaType.APPLICATION_JSON_VALUE)
    public Double getSumOwnerPayments(
            @PathVariable Integer id) {
        Double sum = PaymentsCalculator.sumOwnerPayments(id, ownerRepository);
        message.getInfo("getSumOwnerPayments()", sum, "ownerId", id);
        return sum;
    }
    @GetMapping(value = "/{id}/payments/sum/{kind}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Double getSumOwnerPaymentsByKind(
            @PathVariable Integer id,
            @PathVariable String kind) {
        Double sum = PaymentsCalculator.sumOwnerPaymentsByKind(id, kind, ownerRepository);
        message.getInfo("getSumOwnerPaymentsByKind()", sum, "ownerId", id, "kind", kind);
        return sum;
    }
    @PostMapping()
    void addOwner(@RequestBody Owner body) {
        message.getInfo("addOwner()", body);
        ownerRepository.save(body);
    }
    @DeleteMapping("/{id}")
    void deleteOwner(@PathVariable Integer id) {
        message.getInfo("deleteOwner()", ownerRepository.findOwnerById(id));
        ownerRepository.delete(ownerRepository.findOwnerById(id));
    }
}
