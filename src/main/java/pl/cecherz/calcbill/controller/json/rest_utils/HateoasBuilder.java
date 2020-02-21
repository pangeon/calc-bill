package pl.cecherz.calcbill.controller.json.rest_utils;

import org.springframework.hateoas.EntityModel;
import pl.cecherz.calcbill.controller.json.v3.OwnerController;
import pl.cecherz.calcbill.controller.json.v3.PaymentsController;
import pl.cecherz.calcbill.model.json.Owner;
import pl.cecherz.calcbill.model.json.Payments;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HateoasBuilder {
    public static EntityModel<Owner> mapToEnityModel(Owner owner) {
        EntityModel<Owner> ownerWithSelfLinks = new EntityModel<>(owner);
        ownerWithSelfLinks.add(linkTo(methodOn(OwnerController.class).getOwner(owner.getId())).withSelfRel());
        return ownerWithSelfLinks;
    }
    public static EntityModel<Payments> mapToEnityModel(Payments payments) {
        EntityModel<Payments> paymentsWithSelfLinks = new EntityModel<>(payments);
        paymentsWithSelfLinks.add(linkTo(methodOn(PaymentsController.class).getPayment(payments.getId())).withSelfRel());
        return paymentsWithSelfLinks;
    }
}
