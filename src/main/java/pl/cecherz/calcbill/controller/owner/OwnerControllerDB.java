package pl.cecherz.calcbill.controller.owner;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.cecherz.calcbill.model.db.Owner;
import pl.cecherz.calcbill.repositories.OwnerRepository;
import pl.cecherz.calcbill.utils.MessageBuilder;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/api/owners")
@Component("OwnerControllerDB")
public class OwnerControllerDB {
    /* Wprowadzenie identyfikacji klasy dla narzędzia MessageBuilder */
    private MessageBuilder message = new MessageBuilder(OwnerControllerDB.class);

    /* Dane zapisane z użyciem obiektu Owner repository trafiają do bazy danych, do pamięci trwałej */
    private final OwnerRepository ownerRepository;

    private Collection<Owner> owners = new ArrayList<>();
    {
        message.getInfo("Init owners collection:", owners);
    }

    public OwnerControllerDB(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }
    @GetMapping("/select")
    public Iterable<Owner> selectAllOwners() {
        message.getInfo(
                "selectAllOwners()",
                ownerRepository.findAll()
        );
        return ownerRepository.findAll();
    }
    /* Dane w formie JSON-a zostają pobrane z bazy danych:
    odpowiednik zapytania SELECT * FROM owners WHERE owners.id = ?;
    Nie mamy dostępu do kolekcji Payments, tabela owner nie przechowuje tych odnośnie płatności. Nie zawiera relacji.

    Gdy odwołamy się do rekordu, który nie istneje w tabeli metoda rzuci wyjątek: NullPointerException
    Zwracany jest obiekt nie jest lista gdyż id jest unikatowe */
    @GetMapping("/select/id/{id}")
    public Owner findOwnerByID(@PathVariable Integer id) {
        if (ownerRepository.findOwnerById(id) == null) {
            throw new NullPointerException();
        }
        message.getInfo(
                "findOwnerById: ",
                ownerRepository.findOwnerById(id)
        );
        return ownerRepository.findOwnerById(id);
    }
    /* Dane w formie JSON-a zostają pobrane z bazy danych:
    odpowiednik zapytania SELECT * FROM owners WHERE owners.name = ?;
    Zwracana jest lista, imiona mogą się powtarzać. */
    @GetMapping("/select/name/{name}")
    public Iterable<Owner> findOwnerByName(@PathVariable String name) {
        if (ownerRepository.findOwnerByName(name) == null) {
            throw new NullPointerException();
        }
        message.getInfo(
                "findOwnerByName: ",
                ownerRepository.findOwnerByName(name)
        );
        return ownerRepository.findOwnerByName(name);
    }
    /* Dane w formie JSON-a zostają pobrane z bazy danych:
    odpowiednik zapytania SELECT * FROM owners WHERE owners.surname = ?;
    Zwracana jest lista, imiona mogą się powtarzać.*/
    @GetMapping("/select/surname/{surname}")
    public Iterable<Owner> findOwnerBySurame(@PathVariable String surname) {
        if (ownerRepository.findOwnerBySurname(surname) == null) {
            throw new NullPointerException();
        }
        message.getInfo(
                "findOwnerBySurname: ",
                ownerRepository.findOwnerBySurname(surname)
        );
        return ownerRepository.findOwnerBySurname(surname);
    }
    /* Dane w formie JSON-a zostają zapisane w bazie danych: opowiednik zapytania INSERT INTO owner (id, name, surname) VALUES (...)
    Podanie id jest koniecznie mimo iż model zawiera właściwość @GeneratedValue(strategy = GenerationType.IDENTITY)
    - Dodaje najwyższe możliwe id większe o jeden od ostatniego
    - Napisuje wartość gdy podamy istniejące id */
    @PostMapping("/insert")
    void insertOwner(@RequestBody Owner body) {
        message.getInfo("insertOwner()", body);
        ownerRepository.save(body);
    }

}
