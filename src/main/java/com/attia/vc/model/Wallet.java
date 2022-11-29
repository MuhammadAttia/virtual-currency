package com.attia.vc.model;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "VC_WALLET")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FUND")
    private BigDecimal fund;

    @Column(name = "LAST_ACCRUE_DATE")
    private LocalDateTime lastAccrueDate;

    @Column(name = "UUID")
    private String UUID;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "wallet")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getFund() {
        return fund;
    }

    public void setFund(BigDecimal fund) {
        this.fund = fund;
    }

    public LocalDateTime getLastAccrueDate() {
        return lastAccrueDate;
    }

    public void setLastAccrueDate(LocalDateTime lastAccrueDate) {
        this.lastAccrueDate = lastAccrueDate;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
