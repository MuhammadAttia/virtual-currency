package com.attia.vc.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "VC_TRANSACTION")
@EntityListeners(AuditingEntityListener.class)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "REFERENCE_NUMBER")
    private String referenceNumber;

    @Column(name = "SENDER_UUID")
    private String senderUUID;

    @Column(name = "RECEIVER_UUID")
    private String receiverUUID;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @CreatedDate
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "transaction")
    private Set<UserHasTransaction> userHasTransaction = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderUUID() {
        return senderUUID;
    }

    public void setSenderUUID(String senderUUID) {
        this.senderUUID = senderUUID;
    }

    public String getReceiverUUID() {
        return receiverUUID;
    }

    public void setReceiverUUID(String receiverUUID) {
        this.receiverUUID = receiverUUID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<UserHasTransaction> getUserHasTransaction() {
        return userHasTransaction;
    }

    public void setUserHasTransaction(Set<UserHasTransaction> bookPublishers) {
        this.userHasTransaction = bookPublishers;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
}
