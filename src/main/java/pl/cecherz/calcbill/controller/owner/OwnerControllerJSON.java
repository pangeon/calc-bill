package pl.cecherz.calcbill.controller.owner;
;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.cecherz.calcbill.model.Owner;
import pl.cecherz.calcbill.model.Payments;
import pl.cecherz.calcbill.utils.MessageBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/owners")
@Component("OwnerControllerJSON")
public class OwnerControllerJSON {
    /* Wprowadzenie identyfikacji klasy dla narzędzia MessageBuilder */
    private MessageBuilder message = new MessageBuilder(OwnerControllerJSON.class);

    /* Dane zapisane w postaci listy przechowywane są tylko w pamięci tymczasowej */
    private Collection<Owner> owners = new ArrayList<>();
    {
        message.getInfo("Init owners collection:", owners);
    }

    /* Zwraca JSON-a reprezentację kolekcji z obiektami użytkowników. */
    @GetMapping()
    public Iterable<Owner> getAllOwners() {
        message.getInfo(
                "getAllOwners()",
                owners
        );
        return owners;
    }
    /* Zwraca JSON-a wartość jednego pola będącego zagnieżdżoną tablicą i reprezentacją obiektu Payments */
    @GetMapping("{id}/payments")
    public Optional<List<Payments>> getOwnerPayment(@PathVariable int id) {
        var payments = owners.stream().filter(owner -> owner.getId() == id)
                .findAny().map(Owner::getPayments);
        message.getInfo("getOwnerPayment()", payments);
        return payments;
    }
    /* Zwraca JSON-a reprezentację jednego obiektu użytkownika. */
    @GetMapping("/{id}")
    public Optional<Owner> getOwner(@PathVariable int id) {
        Optional<Owner> filteredOwners = owners.stream().filter(owner -> owner.getId() == id).findAny();
        message.getInfo("getOwner()", filteredOwners);
        return filteredOwners;
    }
    /* Dodaje użytkownika do kolekcji: { "id": 1, "payments": [], "name": "Kamil", "surname": "Cecherz" } */
    @PostMapping()
    public void addOwner(@RequestBody Owner owner) {
        message.getInfo("addOwner()", owner);
        owners.add(owner);
    }
    /* JSON Dodaje płatność do utworzonego użytkownika, nie nadpisuje gotowych danych !
    Do utworzenia płatności służy osobny JSON: przekazywany jest obiekt typu Payment
    za pomocą adnotacji @RequestBody.

    @PathVariable wskazuje na id utworzonego użytkownika, któremu przypisujemy płatność
    { "id": 3, "kind": "home", "amount": 20.0, "date": null } */
    @PostMapping (value = "/{owner_id}/payments" /*,consumes = MediaType.TEXT_PLAIN_VALUE */)
    public void addPaymentToOwner(@PathVariable int owner_id, @RequestBody Payments payment) {
        owners.stream().filter(owner -> owner.getId() == owner_id)
                .findAny()
                .ifPresent(owner -> owner.getPayments().add(payment));
        message.getInfo("addPaymentToOwner()",
                "owner_id: ", owner_id,
                "payment: ", payment,
                owners);
    }
}
