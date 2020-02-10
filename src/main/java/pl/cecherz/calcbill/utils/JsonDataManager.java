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
        owners.add(createOwner(2,"Łukasz", "Bednarski"));
        owners.add(createOwner(3,"Agnieszka", "Lasota"));
        owners.add(createOwner(4,"Robert", "Śliwiński"));
        owners.add(createOwner(5,"Roman", "Giertych"));
        owners.add(createOwner(6,"Dionizy", "Złopolski"));
        owners.add(createOwner(7,"Anna", "Jantar"));
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
        payments.add(createPayment(7,4,"sex", 34.12));
        payments.add(createPayment(8,4,"food", 4.09));
        payments.add(createPayment(9,4,"events", 348.33));
        payments.add(createPayment(10,4,"events", 90.00));
        payments.add(createPayment(11,5,"medicine", 10.00));
        payments.add(createPayment(12,5,"food", 34.34));
        payments.add(createPayment(13,5,"events", 1450.90));
        payments.add(createPayment(14,5,"home", 510.22));
        payments.add(createPayment(15,5,"home", 5.08));
        payments.add(createPayment(16,6,"transport", 555.08));
        payments.add(createPayment(17,6,"toys", 1.02));
        payments.add(createPayment(18,6,"spa", 1207.88));
        payments.add(createPayment(19,6,"home", 100.02));
        payments.add(createPayment(20,6,"home", 85.09));
        return payments;
    }
}

