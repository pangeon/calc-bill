package pl.cecherz.calcbill.utils;

import pl.cecherz.calcbill.model.json.Owner;
import pl.cecherz.calcbill.model.json.Payments;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class JsonDataManager {
    private static Owner createOwner(Integer id, String name, String surname) {
        Owner owner = new Owner();
        List<Payments> payments = new ArrayList<>();
        owner.setId(id);
        owner.setName(name);
        owner.setSurname(surname);
        owner.setPayments(payments);
        return owner;
    }
    private static Payments createPayment(Integer id, Integer ownerId, String kind, Double amount) {
        Payments payment = new Payments();
        payment.setId(id);
        payment.setOwnerId(ownerId);
        payment.setKind(kind);
        payment.setAmount(amount);
        payment.setDate(new Timestamp(System.currentTimeMillis()));
        return payment;
    }

    public static List<Owner> initOwners() {
        ArrayList<Owner> owners = new ArrayList<>();
        owners.add(createOwner(1,"Kamil", "Cecherz"));
        owners.add(createOwner(2, "Łukasz", "Bednarski"));
        owners.add(createOwner(3,"Agnieszka", "Lasota"));
        return owners;
    }
    public static List<Payments> initPayments() {
        ArrayList<Payments> payments = new ArrayList<>();
        payments.add(createPayment(1,1,"medicine", 101.01));
        payments.add(createPayment(2,1,"toys", 205.77));
        payments.add(createPayment(3,2,"medicine", 34.99));
        payments.add(createPayment(4,2,"toys", 22.15));
        payments.add(createPayment(5,2,"home", 5.77));
        payments.add(createPayment(6,3,"sex", 64.02));
        return payments;
    }
}

