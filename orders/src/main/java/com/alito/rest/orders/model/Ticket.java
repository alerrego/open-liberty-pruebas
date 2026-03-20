package com.alito.rest.orders.model;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="tickets",uniqueConstraints=@UniqueConstraint(columnNames={"event_id","dni"})) //EL @UniqueConstraint LO USO PARA QUE EN CADA EVENTO HAYA UNA UNICA ENTRADA POR DNI
public class Ticket {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="event_id",nullable=false)
    private Long eventId;

    @Column(name="dni",nullable=false)
    private String dni;

    @Column(name="guest_name",nullable=false)
    private String guestName;

    @Column(name="ticket_type",nullable=false)
    private String ticketType;

    @ManyToOne
    @JoinColumn(name="order_id",nullable=false)
    @JsonbTransient //SIRVE PARA CORTAR EL CICLO INFINITO AL ENVIAR JSON
    private Order order;

    public Ticket() {
    }

    public Ticket(Long eventId, String dni, String guestName, String ticketType, Order order) {
        this.eventId = eventId;
        this.dni = dni;
        this.guestName = guestName;
        this.ticketType = ticketType;
        this.order = order;
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    


}
