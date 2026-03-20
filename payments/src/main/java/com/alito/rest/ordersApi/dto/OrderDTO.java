package com.alito.rest.ordersApi.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    private Long id;
    private Long eventId;
    private Long buyerUserId;
    private LocalDateTime purchaseDate;
    private String status;
    private Double totalAmount;
    private List<TicketDTO> tickets;
    
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
    public Double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public List<TicketDTO> getTickets() {
        return tickets;
    }
    public void setTickets(List<TicketDTO> tickets) {
        this.tickets = tickets;
    }

    
}
