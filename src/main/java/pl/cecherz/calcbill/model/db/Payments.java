package pl.cecherz.calcbill.model.db;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    private Integer id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OWNER_ID",
            foreignKey=@ForeignKey(name="OWNER_ID"),
            nullable = false) // TO FIXED: tymczasowe usunięcie ograniczenia z bazy danych
    private Owner ownerId;

    @Column(name = "KIND", nullable = false, columnDefinition = "TINYTEXT")
    private String kind;

    @Column(name = "AMOUNT", nullable = false)
    private Double amount;

    @Column(name = "DATE", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp date;

    public Payments() {}

    public Payments(Owner ownerId, String kind, Double amount, Timestamp date) {
        this.ownerId = ownerId;
        this.kind = kind;
        this.amount = amount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Owner getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Owner ownerId) {
        this.ownerId = ownerId;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
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
                getKind().equals(payments.getKind()) &&
                getDate().equals(payments.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),
                getKind(), getAmount(), getDate());
    }

    @Override
    public String toString() {
        return "Payments{" +
                "id=" + id +
                ", kind='" + kind + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
