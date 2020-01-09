package pl.cecherz.calcbill.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "PAYMENTS")
public class Payments implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "OWNER_ID", foreignKey=@ForeignKey(name="OWNER_ID"), nullable = false)
    private Owner owner_id;

    @Column(name = "KIND", nullable = false, columnDefinition = "TINYTEXT")
    private String kind;

    @Column(name = "AMOUNT", nullable = false)
    private double amount;

    @Column(name = "DATE", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Owner getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(Owner owner_id) {
        this.owner_id = owner_id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payments)) return false;
        Payments payments = (Payments) o;
        return getId() == payments.getId() &&
                Double.compare(payments.getAmount(), getAmount()) == 0 &&
                getOwner_id().equals(payments.getOwner_id()) &&
                getKind().equals(payments.getKind()) &&
                getDate().equals(payments.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwner_id(), getKind(), getAmount(), getDate());
    }

    @Override
    public String toString() {
        return "Payments{" +
                "id=" + id +
                ", owner_id=" + owner_id +
                ", kind='" + kind + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
