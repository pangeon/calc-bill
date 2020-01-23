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
import pl.cecherz.calcbill.repositories.OwnerRepository;
import pl.cecherz.calcbill.utils.MessageBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/owners")
@Component("OwnerController")
public class OwnerController {
    /* Wprowadzenie identyfikacji klasy dla narzędzia MessageBuilder */
    static {
        MessageBuilder.setTargetClass(OwnerController.class);
    }
    /* Dane zapisane z użyciem obiektu Owner repository trafiają do bazy danych, do pamięci trwałej */
    private final OwnerRepository ownerRepository;

    /* Dane zapisane w postaci listy przechowywane są tylko w pamięci tymczasowej */
    private Collection<Owner> owners = new ArrayList<>();

    public OwnerController(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    /* Zwraca JSON-a reprezentację kolekcji z obiektami użytkowników. */
    @GetMapping()
    public Iterable<Owner> getOwnersSequence() {
        MessageBuilder.getInfo(
                "getOwnersSequence()",
                owners
        );
        return owners;
    }
    /* Zwraca JSON-a wartość jednego pola będącego zagnieżdżoną tablicą i reprezentacją obiektu Payments */
    @GetMapping("{id}/payments")
    public Optional<List<Payments>> getOwnerPayment(@PathVariable int id) {
        return owners.stream().filter(owner -> owner.getId() == id)
                .findAny().map(Owner::getPayments);
    }
    /* Zwraca JSON-a reprezentację jednego obiektu użytkownika. */
    @GetMapping("/{id}")
    public Optional<Owner> getOwner(@PathVariable int id) {
        return owners.stream().filter(owner -> owner.getId() == id).findAny();
    }
    /* Dane w formie JSON-a zostają pobrane z bazy danych: odpowiednik zapytania SELECT * FROM owners;
    Nie mamy dostępu do kolekcji Payments, tabela owner nie przechowuje tych odnośnie płatności. Nie zawiera relacji. */
    @GetMapping("/select")
    public Iterable<Owner> selectAllOwnersFromDatabase() {
        MessageBuilder.getInfo(
                "selectAllOwnersFromDatabase()",
                ownerRepository.findAll()
        );
        return ownerRepository.findAll();
    }
    /* Dane w formie JSON-a zostają pobrane z bazy danych: odpowiednik zapytania SELECT * FROM owners WHERE owners.id = ?;
    Nie mamy dostępu do kolekcji Payments, tabela owner nie przechowuje tych odnośnie płatności. Nie zawiera relacji.

    Gdy odwołamy się do rekordu, który nie istneje w tabeli metoda rzuci wyjątek: NullPointerException
    Zwracany jest obiekt nie jest lista gdyż id jest unikatowe */
    @GetMapping("/select/id/{id}")
    public Owner findOwnerFromDatabaseByID(@PathVariable Integer id) {
        if (ownerRepository.findOwnerById(id) == null) {
            throw new NullPointerException();
        }
        MessageBuilder.getInfo(
                "findOwnerFromDatabaseID: ",
                ownerRepository.findOwnerById(id)
        );
        return ownerRepository.findOwnerById(id);
    }
    /* Dane w formie JSON-a zostają pobrane z bazy danych: odpowiednik zapytania SELECT * FROM owners WHERE owners.name = ?;
    Zwracana jest lista, imiona mogą się powtarzać. */
    @GetMapping("/select/name/{name}")
    public Iterable<Owner> findOwnerFromDatabaseByName(@PathVariable String name) {
        if (ownerRepository.findOwnerByName(name) == null) {
            throw new NullPointerException();
        }
        MessageBuilder.getInfo(
                "findOwnerFromDatabaseByName: ",
                ownerRepository.findOwnerByName(name)
        );
        return ownerRepository.findOwnerByName(name);
    }
    /* Dane w formie JSON-a zostają pobrane z bazy danych: odpowiednik zapytania SELECT * FROM owners WHERE owners.surname = ?;
    Zwracana jest lista, imiona mogą się powtarzać.*/
    @GetMapping("/select/surname/{surname}")
    public Iterable<Owner> findOwnerFromDatabaseBySurame(@PathVariable String surname) {
        if (ownerRepository.findOwnerBySurname(surname) == null) {
            throw new NullPointerException();
        }
        MessageBuilder.getInfo(
                "findOwnerFromDatabaseBySurname: ",
                ownerRepository.findOwnerBySurname(surname)
        );
        return ownerRepository.findOwnerBySurname(surname);
    }

    /* Dodaje użytkownika do kolekcji:
    {
        "id": 1,
        "payments": [],
        "name": "Kamil",
        "surname": "Cecherz"
    }
     */
    @PostMapping()
    public void addOwner(@RequestBody Owner owner) {
        owners.add(owner);
    }

    /* Dane w formie JSON-a zostają zapisane w bazie danych: opowiednik zapytania INSERT INTO owner (id, name, surname) VALUES (...)
    Podanie id jest koniecznie mimo iż model zawiera właściwość @GeneratedValue(strategy = GenerationType.IDENTITY)
    - Dodaje najwyższe możliwe id większe o jeden od ostatniego
    - Napisuje wartość gdy podamy istniejące id */
    @PostMapping("/insert")
    public Owner insertOwnerToDatabase(@RequestBody Owner body) {
        return ownerRepository.save(body);
    }

    /* JSON Dodaje płatność do utworzonego użytkownika, nie napisuje gotowych danych !
    Do utworzenia płatności służy osobny JSON: przekazywany jest obiekt typu Payment
    za pomocą adnotacji @RequestBody.

    @PathVariable wskazuje na id utworzonego użytkownika, któremu przypisujemy płatność
    {
        "id": 3,
        "kind": "home",
        "amount": 20.0,
        "date": null
    } */
    @PostMapping (value = "/{owner_id}/payments" /*,consumes = MediaType.TEXT_PLAIN_VALUE */)
    public void addPaymentToOwner(@PathVariable int owner_id, @RequestBody Payments payment) {
        owners.stream().filter(owner -> owner.getId() == owner_id).findAny().ifPresent(owner -> {
                    owner.getPayments().add(payment);
        });
    }
}
