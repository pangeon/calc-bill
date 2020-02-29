package pl.cecherz.calcbill.controller.db;

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
        message.getInfo("selectAllOwners()", ownerRepository.findAll());
        return ownerRepository.findAll();
    }
    @GetMapping("/{id}")
    public Owner getOwner(@PathVariable Integer id) {
        if (ownerRepository.findOwnerById(id) == null) {
            throw new NullPointerException();
        }
        message.getInfo("findOwnerById: ", ownerRepository.findOwnerById(id));
        return ownerRepository.findOwnerById(id);
    }
    @PostMapping()
    void addOwner(@RequestBody Owner body) {
        message.getInfo("insertOwner()", body);
        ownerRepository.save(body);
    }
}
