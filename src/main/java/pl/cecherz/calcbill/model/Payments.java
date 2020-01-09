package pl.cecherz.calcbill.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "PAYMENTS")
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "PAY_OWNER")
    private Payments PAY_OWNER;

    @Column(name = "KIND", nullable = false)
    private String kind;

    @Column(name = "AMOUNT", nullable = false)
    private double amount;

    @Column(name = "DATE", nullable = false)
    private Date date;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Payments getPAY_OWNER() {
        return PAY_OWNER;
    }
    public void setPAY_OWNER(Payments PAY_OWNER) {
        this.PAY_OWNER = PAY_OWNER;
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
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payments)) return false;
        Payments payments = (Payments) o;
        return getId() == payments.getId() &&
                Double.compare(payments.getAmount(), getAmount()) == 0 &&
                Objects.equals(getPAY_OWNER(), payments.getPAY_OWNER()) &&
                Objects.equals(getKind(), payments.getKind()) &&
                Objects.equals(getDate(), payments.getDate());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPAY_OWNER(), getKind(), getAmount(), getDate());
    }
    @Override
    public String toString() {
        return "Payments{" +
                "id=" + id +
                ", PAY_OWNER=" + PAY_OWNER +
                ", kind='" + kind + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
