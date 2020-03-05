package pl.cecherz.calcbill.controller.db;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pl.cecherz.calcbill.exeptions.EmptyFindResultException;
import pl.cecherz.calcbill.exeptions.EntityEmptyContentException;
import pl.cecherz.calcbill.exeptions.EntityNotFoundException;
import pl.cecherz.calcbill.exeptions.RestExceptionHandler;
import pl.cecherz.calcbill.model.db.Owner;
import pl.cecherz.calcbill.model.db.Payments;
import pl.cecherz.calcbill.repositories.OwnerRepository;
import pl.cecherz.calcbill.utils.MessageBuilder;
import pl.cecherz.calcbill.utils.PaymentsCalculator;

import java.util.List;
import java.util.Optional;
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
public class OwnerController extends RestExceptionHandler {
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
        Owner owner = ownerRepository.findOwnerById(id);
        if (owner == null) throw new EntityNotFoundException(id);
        message.getInfo("getOwner()", owner, "status", HttpStatus.OK);
        return owner;
    }
    @GetMapping("/{id}/payments")
    public List<Payments> getOwnerPayments(
            @PathVariable Integer id) {
        Owner owner = ownerRepository.findOwnerById(id);
        if (owner == null) throw new EntityNotFoundException(id);

        List<Payments> ownerPayments = owner.getPayments();
        if (ownerPayments.isEmpty()) throw new EntityEmptyContentException(id);

        message.getInfo("getOwnerPayments()", ownerPayments);
        return ownerPayments;
    }
    @GetMapping("/{id}/payments/{kind}")
    public List<Payments> getOwnerPaymentsByKind(
            @PathVariable Integer id,
            @PathVariable String kind) {
        Owner owner = ownerRepository.findOwnerById(id);
        if (owner == null) throw new EntityNotFoundException(id);

        final List<Payments> ownerPaymentsByKind = owner.getPayments()
                .stream().filter(payment -> payment.getKind().equals(kind))
                .collect(Collectors.toList());
        if (ownerPaymentsByKind.isEmpty()) throw new EmptyFindResultException(id, kind);

        message.getInfo("getOwnerPaymentsByKind()", ownerPaymentsByKind);
        return ownerPaymentsByKind;
    }
    @GetMapping("/{id}/payments/{min}/{max}")
    public List<Payments> getOwnerPaymentsByAmonutRange(
            @PathVariable Integer id,
            @PathVariable Double min,
            @PathVariable Double max) {
        Owner owner = ownerRepository.findOwnerById(id);
        if (owner == null) throw new EntityNotFoundException(id);

        final List<Payments> ownerPaymentsByAmountRange = ownerRepository.findOwnerById(id).getPayments()
                .stream().filter(payment -> payment.getAmount() > min && payment.getAmount() < max)
                .collect(Collectors.toList());
        String range = min + " - " + max;
        if(ownerPaymentsByAmountRange.isEmpty()) throw new EmptyFindResultException(id, range);
        message.getInfo("getOwnerPaymentsByAmonutRange()", ownerPaymentsByAmountRange);
        return ownerPaymentsByAmountRange;
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
    @ResponseStatus(HttpStatus.CREATED)
    public void addOwner(@RequestBody Owner body) {
        message.getInfo("addOwner()", body);
        ownerRepository.save(body);
    }
    @PutMapping("/{id}")
    public void replaceOwner(
            @PathVariable Integer id,
            @RequestBody Owner newOwner) {
        Optional<Owner> ownerValuesToReplace = ownerRepository.findById(id);
        if (ownerValuesToReplace.orElse(null) == null) throw new EntityNotFoundException(id);
        message.getInfo("start :: replaceOwner()", ownerValuesToReplace);
        ownerValuesToReplace.ifPresent(owner -> {
            owner.setId(id);
            owner.setName(newOwner.getName());
            owner.setSurname(newOwner.getSurname());
        });
        ownerRepository.save(ownerValuesToReplace.orElse(null));
        message.getInfo("end :: replaceOwner()", newOwner);
    }
    @PatchMapping("/{id}")
    public void updateOwner(
            @PathVariable Integer id,
            @RequestBody Owner ownerToUpdate) {
        Optional<Owner> ownerValuesToUpdate = ownerRepository.findById(id);
        if (ownerValuesToUpdate.orElse(null) == null) throw new EntityNotFoundException(id);
        message.getInfo("start :: updateOwner()", ownerValuesToUpdate);
        ownerValuesToUpdate.ifPresent(owner -> {
            if(ownerToUpdate.getId() != null) owner.setId(id);
            if(ownerToUpdate.getName() != null) owner.setName(ownerToUpdate.getName());
            if(ownerToUpdate.getSurname() != null) owner.setSurname(ownerToUpdate.getSurname());
        });
        ownerRepository.save(ownerValuesToUpdate.orElse(null));
        message.getInfo("end :: updateOwner()", ownerValuesToUpdate);
    }
    @DeleteMapping("/{id}")
    public void deleteOwner(@PathVariable Integer id) {
        Owner ownerToDelete = ownerRepository.findOwnerById(id);
        if(ownerToDelete == null) throw new EntityNotFoundException(id);
        message.getInfo("deleteOwner()", ownerToDelete);
        ownerRepository.delete(ownerToDelete);
    }
}
