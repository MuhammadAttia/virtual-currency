package com.attia.vc.model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "VC_USER_HAS_TRANSACTION")
public class UserHasTransaction implements Serializable {

    @EmbeddedId
    private UserHasTransactionId id = new UserHasTransactionId();

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("transactionId")
    private Transaction transaction;

    @Enumerated(EnumType.STRING)
    @Column(name = "TRANSACTION_TYPE")
    private TransactionType type = TransactionType.SEND;

    public UserHasTransactionId getId() {
        return id;
    }

    public void setId(UserHasTransactionId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

}
