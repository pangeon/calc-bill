package pl.cecherz.calcbill.controller.json.v3;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.cecherz.calcbill.controller.json.rest_utils.HTTPHeaderUtils;
import pl.cecherz.calcbill.controller.json.rest_utils.HateoasBuilder;
import pl.cecherz.calcbill.model.json.Owner;
import pl.cecherz.calcbill.model.json.Payments;
import pl.cecherz.calcbill.utils.CSS_Style;
import pl.cecherz.calcbill.utils.ContentHTMLWrapper;
import pl.cecherz.calcbill.utils.JsonDataManager;
import pl.cecherz.calcbill.utils.MessageBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v3/owners")
@Component("OwnerControllerJSON-V3")
public class OwnerController extends HTTPHeaderUtils {
    /* Wprowadzenie identyfikacji klasy dla narzędzia MessageBuilder */
    private MessageBuilder message = new MessageBuilder(OwnerController.class);

    /* Dane zapisane w postaci listy przechowywane są tylko w pamięci tymczasowej */
    private List<Owner> ownersList = JsonDataManager.initOwners();

    /* Zwraca JSON-a reprezentację kolekcji z obiektami użytkowników.
    Przechowuje informacje w cachu przez 5 minut */
    @GetMapping
    public ResponseEntity<List<Owner>> getAllOwners() {
        message.getInfo("V3 :: getAllOwners()", ownersList);
        return ResponseEntity.ok().header("Cache-Control", "max-age" + "=300").body(ownersList);
    }
    /* Zwraca listę użytkowników (imię i nazwisko) w postaci HTML-a */
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE, params = {"color", "fontsize"})
    public String getHTMLOwnersNamesandSurname(@RequestParam("color") String color, @RequestParam("fontsize") String fontsize) {
        final String collect = ownersList.stream()
                .flatMap(owner -> Stream.of("<p>" + owner.getName(), owner.getSurname() + "</p>"))
                .collect(Collectors.joining("\n"));

        CSS_Style style = new CSS_Style();
        style.addParamStyle(color, fontsize);
        String pageHTMLwithOwnersNameAndSurname = ContentHTMLWrapper.wrapToHTML(collect, "Owners name", style.getStyle());

        message.getInfo("V3 :: getHTMLOwnersNamesandSurname()", pageHTMLwithOwnersNameAndSurname);
        return pageHTMLwithOwnersNameAndSurname;
    }
    /* Zwraca JSON-a wartość jednego pola będącego zagnieżdżoną tablicą i reprezentacją obiektu Payments
    Zwraca kod 200 - OK, 204 - NO CONTENT, kod 404 - NOT FOUND */
    @GetMapping(value = "/payments", params = "id")
    public ResponseEntity<Optional<List<Payments>>> getOwnerPayment(@RequestParam("id") Integer id) {
        var payments = findOwnerById(id).map(Owner::getPayments);
        if(findOwnerById(id).isEmpty()) {
            message.getInfo("V3 :: getOwnerPayment()", payments, "status", HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            if (payments.toString().equals("Optional[[]]")) {
                message.getInfo("V3 :: getOwnerPayment()", payments, "status", HttpStatus.NO_CONTENT);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                message.getInfo("V3 :: getOwnerPayment()", payments, "status", HttpStatus.OK);
                return ResponseEntity.ok().body(payments);
            }
        }
    }
    /* Zwraca JSON-a reprezentację jednego obiektu użytkownika.
    Zwraca kod 200 - OK lub kod 404 - NOT FOUND
    Dodaje self-referencje do zasobu w postaci linku */
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Owner>> getOwner(@PathVariable("id") Integer id) {
        var filteredOwners = findOwnerById(id);
        if (findOwnerById(id).isEmpty()) {
            message.getInfo("V3 :: getOwner()", filteredOwners, "status", HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            message.getInfo("V3 :: getOwner()", filteredOwners, "status", HttpStatus.OK);
            return ResponseEntity.ok()
                    .body(filteredOwners.map(HateoasBuilder::mapToEnityModel).orElse(null));
        }
    }
    /* Zastępuje wartości obiektu Owner - w przypadku kolekcji w momencie zmiany pola pozostałe zmienia na null
    *  Obiekt Owner przechowuje obiekty Payments, w formacie JSON zagnieżdzenie jest reprezentowane w postaci tablicy:
    * { "id": 4,  "payments": [ { "id": ..., "ownerId": ..., "kind": "...", "amount": ..., "date": "..." } ],
    "name": "Marcin", "surname": "Luter"}
    Zwraca kod 200 - OK lub kod 404 - NOT FOUND */
        @PutMapping("/{id}")
        public ResponseEntity<?> replaceOwner(@PathVariable Integer id, @RequestBody Owner newOwner) {
            if (findOwnerById(id).isEmpty()) {
                message.getInfo("V3 :: replaceOwner()", "status", HttpStatus.NOT_FOUND);
                return ResponseEntity.notFound().build();
            } else {
                findOwnerById(id).ifPresent(owner -> {
                    owner.setId(newOwner.getId());
                    owner.setName(newOwner.getName());
                    owner.setSurname(newOwner.getSurname());
                    owner.setPayments(newOwner.getPayments());
                    message.getInfo("V3 :: replaceOwner()", Collections.singletonList(owner), newOwner);
                });
                message.getInfo("V3 :: replaceOwner()", "status", HttpStatus.OK);
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        }
        @PatchMapping("/{id}")
        public ResponseEntity<?> updateOwner(@PathVariable Integer id, @RequestBody Owner ownerValuesToReplace) {
            if (findOwnerById(id).isEmpty()) {
                message.getInfo("V3 :: updateOwner()", "status", HttpStatus.NOT_FOUND);
                return ResponseEntity.notFound().build();
            } else {
                findOwnerById(id).ifPresent(owner -> {
                    if(ownerValuesToReplace.getId() != null) owner.setId(ownerValuesToReplace.getId());
                    if(ownerValuesToReplace.getName() != null) owner.setName(ownerValuesToReplace.getName());
                    if(ownerValuesToReplace.getSurname() != null) owner.setSurname(ownerValuesToReplace.getSurname());
                    if(ownerValuesToReplace.getPayments() != null) owner.setPayments(ownerValuesToReplace.getPayments());
                    message.getInfo("V3 :: updateOwner()", Collections.singletonList(owner), ownerValuesToReplace);
                });
                message.getInfo("V3 :: updateOwner()", "status", HttpStatus.OK);
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        }
        /* --------------------------------------------- */
    /* Dodaje użytkownika do kolekcji: { "id": 1, "payments": [], "name": "Kamil", "surname": "Cecherz" }
    Zwraca kod 201 - CREATED */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public void addOwner(@RequestBody Owner owner) {
        message.getInfo("V3 :: addOwner()", owner, HttpStatus.CREATED);
        ownersList.add(owner);
    }
    /* JSON Dodaje płatność do utworzonego użytkownika, nie nadpisuje gotowych danych !
    Do utworzenia płatności służy osobny JSON: przekazywany jest obiekt typu Payment
    za pomocą adnotacji @RequestBody.

    @PathVariable wskazuje na id utworzonego użytkownika, któremu przypisujemy płatność
    { "id": 3, "kind": "home", "amount": 20.0, "date": null }
    Zwraca kod 201 - CREATED lub kod 404 - NOT FOUND */
    @PostMapping (value = "/id/{id}/payments")
    public ResponseEntity<Object> addPaymentToOwner(@PathVariable Integer id, @RequestBody Payments payment, @RequestHeader HttpHeaders headers) {
        message.getInfo("V3 :: addPaymentToOwner()","params: ", "id: ", id, "payment: ", payment);
        if (findOwnerById(id).isEmpty()) {
            message.getInfo("V3 :: addPaymentToOwner()", "status: " + HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        } else {
            message.getInfo("V3 :: addPaymentToOwner()", "status: " + HttpStatus.CREATED);
            findOwnerById(id).ifPresent(owner -> owner.getPayments().add(payment));
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    }
    /* Usuwa posiadacza z kolekcji po wskazanym id
    Zwraca kod 200 - OK lub kod 404 - NOT FOUND */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOwner(@PathVariable Integer id) {
        message.getInfo("V3 :: deleteOwner()", "id: " + id);
        if(ownersList.removeIf(owner -> owner.getId().equals(id))) {
            message.getInfo("V3 :: deleteOwner()", "status: " + HttpStatus.OK);
            return ResponseEntity.ok().build();
        } else {
            message.getInfo("V3 :: deleteOwner()", "status: " + HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }
    }
    /* Metody prywatne wykorzystywane w API */
    private Optional<Owner> findOwnerById(Integer id) {
        return ownersList.stream()
                .filter(owner -> owner.getId().equals(id))
                .findAny();
    }
    /* --------------------------------------------- */

}
