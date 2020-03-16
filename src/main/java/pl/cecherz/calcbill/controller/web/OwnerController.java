package pl.cecherz.calcbill.controller.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.cecherz.calcbill.controller.web.ui_utils.WebViewBuilder;
import pl.cecherz.calcbill.exeptions.EmptyFindResultException;
import pl.cecherz.calcbill.exeptions.EntityNotFoundException;
import pl.cecherz.calcbill.exeptions.RestExceptionHandler;
import pl.cecherz.calcbill.model.db.Owner;
import pl.cecherz.calcbill.model.db.Payments;
import pl.cecherz.calcbill.repositories.OwnerRepository;
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
 *
 *
 * addOwner()
 * replaceOwner({id})
 * deleteOwner(/delete/{id}) - usuwa dane z bazy: tabele Owner i Payments
 * redirectToAddOwnerDataPage(/add)
 *
 */
@RestController
@RequestMapping("/api/web/owners")
@Component("OwnerControllerWeb")
public class OwnerController extends RestExceptionHandler {
    private final OwnerRepository ownerRepository;

    public OwnerController(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }
    @GetMapping()
    public ModelAndView getAllOwners() {
        ModelAndView ownersListView = new ModelAndView("owners/owners_list");
        ownersListView.addObject("owners", ownerRepository.findAll());
        return ownersListView;
    }
    @GetMapping("/{id}")
    public ModelAndView getOwner(@PathVariable Integer id) {
        ModelAndView ownerView = new ModelAndView("owners/edit_owner_data");
        Owner owner = ownerRepository.findOwnerById(id);
        if (owner == null) throw new EntityNotFoundException(id);

        ownerView.addObject("owner", owner);
        return ownerView;
    }
    @GetMapping("/{id}/payments")
    public ModelAndView getOwnerPayments(@PathVariable Integer id) {
        ModelAndView ownersPaymentsListView = new ModelAndView("owners/owners_payments_list");

        Payments payment = new Payments();
        Owner owner = ownerRepository.findOwnerById(id);
        payment.setOwnerId(owner);
        if (owner == null) throw new EntityNotFoundException(id);
        ownersPaymentsListView.addObject("owner", owner);

        List<Payments> ownerPayments = owner.getPayments();
        ownersPaymentsListView.addObject("ownersPayments", ownerPayments);
        ownersPaymentsListView.addObject("payment", payment);

        return ownersPaymentsListView;
    }
    @GetMapping("/add")
    public ModelAndView redirectToAddOwnerDataPage(Owner owner) {
        return new ModelAndView("owners/add_owner_data");
    }
    @GetMapping(value = "/filter/{id}/payments", params = "kind")
    public ModelAndView getOwnerPaymentsByKind(
            @PathVariable Integer id,
            @RequestParam("kind") String kind) {
        Owner owner = ownerRepository.findOwnerById(id);
        if (owner == null) throw new EntityNotFoundException(id);

        final List<Payments> ownerPaymentsByKind = owner.getPayments()
                .stream().filter(payment -> payment.getKind().equals(kind))
                .collect(Collectors.toList());
        if (ownerPaymentsByKind.isEmpty()) throw new EmptyFindResultException(id, kind);

        return WebViewBuilder.returnOwnerListView(ownerPaymentsByKind);
    }
    @GetMapping("/filter/{id}/payments/{min}/{max}")
    public List<Payments> getOwnerPaymentsByAmonutRange(@PathVariable Integer id, @PathVariable Double min, @PathVariable Double max) {
        Owner owner = ownerRepository.findOwnerById(id);
        if (owner == null) throw new EntityNotFoundException(id);

        final List<Payments> ownerPaymentsByAmountRange = ownerRepository.findOwnerById(id).getPayments()
                .stream().filter(payment -> payment.getAmount() > min && payment.getAmount() < max)
                .collect(Collectors.toList());
        String range = min + " - " + max;
        if(ownerPaymentsByAmountRange.isEmpty()) throw new EmptyFindResultException(id, range);
        return ownerPaymentsByAmountRange;
    }
    @GetMapping(value = "/{id}/payments/sum", produces = MediaType.APPLICATION_JSON_VALUE)
    public Double getSumOwnerPayments(@PathVariable Integer id) {
        return PaymentsCalculator.sumOwnerPayments(id, ownerRepository);
    }
    @GetMapping(value = "/{id}/payments/sum/{kind}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Double getSumOwnerPaymentsByKind(@PathVariable Integer id, @PathVariable String kind) {
        return PaymentsCalculator.sumOwnerPaymentsByKind(id, kind, ownerRepository);
    }
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView addOwner(Owner body) {
        ownerRepository.save(body);
        return WebViewBuilder.returnOwnerListView(ownerRepository);
    }
    @PostMapping("/{id}")
    public ModelAndView replaceOwner(@PathVariable Integer id, Owner newOwner) {
        Optional<Owner> ownerValuesToReplace = ownerRepository.findById(id);
        if (ownerValuesToReplace.orElse(null) == null) throw new EntityNotFoundException(id);
        ownerValuesToReplace.ifPresent(owner -> {
            owner.setId(id);
            owner.setName(newOwner.getName());
            owner.setSurname(newOwner.getSurname());
        });
        ownerRepository.save(ownerValuesToReplace.orElse(null));
        return WebViewBuilder.returnOwnerListView(ownerRepository);
    }
    @GetMapping("/delete/{id}")
    public ModelAndView deleteOwner(@PathVariable Integer id) {
        Owner ownerToDelete = ownerRepository.findOwnerById(id);
        if(ownerToDelete == null) throw new EntityNotFoundException(id);
        ownerRepository.delete(ownerToDelete);

        return WebViewBuilder.returnOwnerListView(ownerRepository);
    }

}
