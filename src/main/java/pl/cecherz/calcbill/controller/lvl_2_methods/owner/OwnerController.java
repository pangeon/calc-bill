package pl.cecherz.calcbill.controller.lvl_2_methods.owner;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.cecherz.calcbill.model.db.Owner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@RestController
@RequestMapping("/api/v2/owners")
@Component("OwnerController_V2")
public class OwnerController {
    // 3.1 Hello World
    @GetMapping("/31")
    public Owner getName() {
        Owner o = new Owner();
        o.setName("Kamil");
        return o;
    }
    // 3.2 Zasoby
    private Collection<Owner> owners = new ArrayList<>();

    @GetMapping("/32/unit")
    public Owner getOwner() {
        Owner o = new Owner();
        o.setName("Kamil");
        o.setSurname("Cecherz");
        return o;
    }
    @GetMapping("/32/list")
    public Iterable<Owner> getOwnerList() {

        return Arrays.asList(
                new Owner("Kamil", "Cecherz"),
                new Owner("Agnieszka", "Lasota"),
                new Owner("Łukasz", "Bednarski")
        );
    }
    @GetMapping("/32/empty-list")
    public Iterable<Owner> getOwnerEmptyList() {
        return owners;
    }
    @PostMapping("/32/add")
    public void addOwnerToList(@RequestBody Owner owner) {
        owners.add(owner);
    }
    // 3.3 Dodawanie zasobów

}
