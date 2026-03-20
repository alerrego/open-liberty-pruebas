package com.alito.rest.orders.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="event_id",nullable=false)
    private Long eventId;

    @Column(name="buyer_user_id",nullable=false)
    private Long buyerUserId;

    @Column(nullable=false)
    private LocalDateTime purchaseDate;

    @Column(nullable=false)
    private String status;

    @Column(name="total_amount",nullable=false)
    private Double totalAmount;

    @OneToMany(mappedBy="order",cascade= CascadeType.ALL)
    private List<Ticket> tickets;

    public Order() {
    }

    public Order(Long eventId, Long buyerUserId, LocalDateTime purchaseDate, String status) {
        this.eventId = eventId;
        this.buyerUserId = buyerUserId;
        this.purchaseDate = purchaseDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getBuyerUserId() {
        return buyerUserId;
    }

    public void setBuyerUserId(Long buyerUserId) {
        this.buyerUserId = buyerUserId;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    


}
