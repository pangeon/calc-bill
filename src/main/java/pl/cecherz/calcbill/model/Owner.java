package pl.cecherz.calcbill.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "OWNER")
public class Owner implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @OneToMany(mappedBy = "owner_id")
    private List<Payments> payments;

    @Column(name = "NAME", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(name = "SURNAME", nullable = false, columnDefinition = "TEXT")
    private String surname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        return getId() == owner.getId() &&
                Objects.equals(getPayments(), owner.getPayments()) &&
                Objects.equals(getName(), owner.getName()) &&
                Objects.equals(getSurname(), owner.getSurname());
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
