package pl.cecherz.calcbill.controller.json.rest_utils;

import org.springframework.hateoas.CollectionModel;
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
    public static void linkToOwnerSelf(CollectionModel<EntityModel<Owner>> collectionModel, String relation) {
        collectionModel.add(linkTo(OwnerController.class).withRel(relation));
    }
    public static void linkToPaymentSelf(CollectionModel<EntityModel<Payments>> collectionModel, String relation) {
        collectionModel.add(linkTo(PaymentsController.class).withRel(relation));
    }
    public static void linkToOwnerId(CollectionModel<EntityModel<Owner>> collectionModel, String methodName, Integer id) {
        collectionModel.add(linkTo(methodOn(OwnerController.class).getOwner(id)).withRel(methodName));
    }
    public static void linkToPaymentId(CollectionModel<EntityModel<Payments>> collectionModel, String methodName, Integer id) {
        collectionModel.add(linkTo(methodOn(PaymentsController.class).getPayment(id)).withRel(methodName));
    }
    public static void queryPaymentKind(CollectionModel<EntityModel<Payments>> collectionModel, String methodName, String kind) {
        collectionModel.add(linkTo(methodOn(PaymentsController.class).filterPaymentsByKind(kind)).withRel(methodName));
    }
    public static void queryPaymentKindAndOwner(CollectionModel<EntityModel<Payments>> collectionModel, String methodName, Integer owner_id, String kind) {
        collectionModel.add(linkTo(methodOn(PaymentsController.class).filterPaymentsByKindAndOwnerId(owner_id, kind)).withRel(methodName));
    }
    public static void queryPaymentOwnerAndAmountRange(CollectionModel<EntityModel<Payments>> collectionModel, String methodName, Integer owner_id, Double min, Double max) {
        collectionModel.add(linkTo(methodOn(PaymentsController.class).filterPaymentsByOwnerIdAndAmonutRange(owner_id, min, max)).withRel(methodName));
    }
}
