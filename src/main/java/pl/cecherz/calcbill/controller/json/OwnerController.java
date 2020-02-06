package pl.cecherz.calcbill.controller.json;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.cecherz.calcbill.model.json.Owner;
import pl.cecherz.calcbill.model.json.Payments;
import pl.cecherz.calcbill.utils.JsonDataManager;
import pl.cecherz.calcbill.utils.MessageBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/owners")
@Component("OwnerControllerJSON")
public class OwnerController {
    /* Wprowadzenie identyfikacji klasy dla narzędzia MessageBuilder */
    private MessageBuilder message = new MessageBuilder(OwnerController.class);

    /* Dane zapisane w postaci listy przechowywane są tylko w pamięci tymczasowej */
    private Collection<Owner> ownersList = JsonDataManager.initOwners();

    /* Zwraca JSON-a reprezentację kolekcji z obiektami użytkowników. */
    @GetMapping()
    public Iterable<Owner> getAllOwners() {
        message.getInfo(
                "getAllOwners()",
                ownersList
        );
        return ownersList;
    }
    /* Zwraca JSON-a wartość jednego pola będącego zagnieżdżoną tablicą i reprezentacją obiektu Payments */
    @GetMapping(value = "/payments", params = "id")
    public Optional<List<Payments>> getOwnerPayment(@RequestParam("id") Integer id) {
        var payments = findOwnerById(id).map(Owner::getPayments);
        message.getInfo("getOwnerPayment()", payments);
        return payments;
    }
    /* Zwraca JSON-a reprezentację jednego obiektu użytkownika. */
    @GetMapping(params = "id")
    public Optional<Owner> getOwner(@RequestParam("id") Integer id) {
        Optional<Owner> filteredOwners = findOwnerById(id);
        message.getInfo("getOwner()", filteredOwners);
        return filteredOwners;
    }

    /* Zastępuje wartości obiektu Owner - w przypadku kolekcji w momencie zmiany pola pozostałe zmienia na null
    *  Obiekt Owner przechowuje obiekty Payments, w formacie JSON zagnieżdzenie jest reprezentowane w postaci tablicy:
    * { "id": 4,  "payments": [ { "id": ..., "ownerId": ..., "kind": "...", "amount": ..., "date": "..." } ],
    "name": "Marcin", "surname": "Luter"} */
        @PutMapping("/{id}")
        public void replaceOwner(@PathVariable Integer id, @RequestBody Owner newOwner) {
            findOwnerById(id).ifPresent(owner -> {
                owner.setId(newOwner.getId());
                owner.setName(newOwner.getName());
                owner.setSurname(newOwner.getSurname());
                owner.setPayments(newOwner.getPayments());
                message.getInfo("replaceOwner()", Collections.singletonList(owner), newOwner);
            });
        }
        @PatchMapping("/{id}")
        public void updateOwner(@PathVariable Integer id, @RequestBody Owner ownerValuesToReplace) {
            findOwnerById(id).ifPresent(owner -> {
                if(ownerValuesToReplace.getId() != null) owner.setId(ownerValuesToReplace.getId());
                if(ownerValuesToReplace.getName() != null) owner.setName(ownerValuesToReplace.getName());
                if(ownerValuesToReplace.getSurname() != null) owner.setSurname(ownerValuesToReplace.getSurname());
                if(ownerValuesToReplace.getPayments() != null) owner.setPayments(ownerValuesToReplace.getPayments());
                message.getInfo("updateOwner()", Collections.singletonList(owner), ownerValuesToReplace);
            });
        }
    /* --------------------------------------------- */

    /* Dodaje użytkownika do kolekcji: { "id": 1, "payments": [], "name": "Kamil", "surname": "Cecherz" } */
    @PostMapping()
    public void addOwner(@RequestBody Owner owner) {
        message.getInfo("addOwner()", owner);
        ownersList.add(owner);
    }
    /* JSON Dodaje płatność do utworzonego użytkownika, nie nadpisuje gotowych danych !
    Do utworzenia płatności służy osobny JSON: przekazywany jest obiekt typu Payment
    za pomocą adnotacji @RequestBody.

    @PathVariable wskazuje na id utworzonego użytkownika, któremu przypisujemy płatność
    { "id": 3, "kind": "home", "amount": 20.0, "date": null } */
    @PostMapping (value = "/id/{id}/payments" /*,consumes = MediaType.TEXT_PLAIN_VALUE */)
    public void addPaymentToOwner(@PathVariable Integer id, @RequestBody Payments payment) {
        findOwnerById(id).ifPresent(owner -> owner.getPayments().add(payment));
        message.getInfo("addPaymentToOwner()",
                "id: ", id,
                "payment: ", payment,
                ownersList);
    }

    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable Integer id) {
        ownersList.removeIf(owner -> owner.getId().equals(id));
    }

    private Optional<Owner> findOwnerById(Integer id) {
        return ownersList.stream()
                .filter(owner -> owner.getId().equals(id))
                .findAny();
    }
}
