package pl.cecherz.calcbill.model.json;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
;

public class Payments implements Serializable {

    private Integer id;
    private Integer ownerId;
    private String kind;
    private Double amount;
    private Timestamp date;

    public Payments(Integer id, Integer ownerId, String kind, Double amount, Timestamp date) {
        this.id = id;
        this.ownerId = ownerId;
        this.kind = kind;
        this.amount = amount;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
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
        return getId().equals(payments.getId()) &&
                getOwnerId().equals(payments.getOwnerId()) &&
                getKind().equals(payments.getKind()) &&
                getAmount().equals(payments.getAmount()) &&
                getDate().equals(payments.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwnerId(), getKind(), getAmount(), getDate());
    }

    @Override
    public String toString() {
        return "Payments{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", kind='" + kind + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
