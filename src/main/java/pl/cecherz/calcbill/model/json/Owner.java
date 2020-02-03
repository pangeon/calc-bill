package pl.cecherz.calcbill.model.json;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


public class Owner implements Serializable {

    private Integer id;
    private List<Payments> payments;
    private String name;
    private String surname;

    public Owner(Integer id, List<Payments> payments, String name, String surname) {
        this.id = id;
        this.payments = payments;
        this.name = name;
        this.surname = surname;
    }
    public Owner() {}
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public List<Payments> getPayments() {
        return payments;
    }
    public void setPayments(List<Payments> payments) {
        this.payments = payments;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Owner)) return false;
        Owner owner = (Owner) o;
        return getId().equals(owner.getId()) &&
                getPayments().equals(owner.getPayments()) &&
                getName().equals(owner.getName()) &&
                getSurname().equals(owner.getSurname());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPayments(), getName(), getSurname());
    }
    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", payments=" + payments +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
