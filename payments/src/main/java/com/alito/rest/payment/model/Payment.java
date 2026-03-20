package com.alito.rest.payment.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="payments")
public class Payment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="order_id",nullable=false)
    private Long orderId;

    @Column(nullable=false)
    private String provider; //ACA SERA MERCADOPAGO,STRIPE,PAYPAL Y ASI

    @Column(name="provider_transaction_id")
    private String providerTransactionId;

    @Column(nullable=false)
    private Double amount;

    @Column(nullable=false)
    private String status;

    @Column(name="crated_at",nullable=false)
    private LocalDateTime createdAt;

    public Payment() {
    }

    public Payment(Long orderId, String provider, String providerTransactionId, Double amount, String status,
            LocalDateTime createdAt) {
        this.orderId = orderId;
        this.provider = provider;
        this.providerTransactionId = providerTransactionId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderTransactionId() {
        return providerTransactionId;
    }

    public void setProviderTransactionId(String providerTransactionId) {
        this.providerTransactionId = providerTransactionId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    
}
