package pl.cecherz.calcbill.model.db;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "OWNER")
public class Owner implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    /*
    fetch = FetchType.EAGER
    Potrzebne by działała metoda OwnerController.selectAllFromDatabase()

    FetchType.EAGER – pobieramy dane, gdy zostaje wykonane zapytanie pobierające nadrzędną część relacji.
    @see: http://nullpointerexception.pl/trzy-rzeczy-ktore-powinienes-wiedziec-o-hibernate/
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "ownerId",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private List<Payments> payments;

    @Column(name = "NAME", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(name = "SURNAME", nullable = false, columnDefinition = "TEXT")
    private String surname;

    public Owner(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

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
