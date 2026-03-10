package com.alito.rest.event.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.alito.rest.ticketType.model.TicketType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="events")
public class Event {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String band;
    private String location;
    private LocalDateTime date;

    @OneToMany(mappedBy="event",cascade=CascadeType.ALL,orphanRemoval=true)
    private List<TicketType> ticketTypes = new ArrayList<>();

    public void addTicketType(TicketType ticketType){
        ticketTypes.add(ticketType);
        ticketType.setEvent(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<TicketType> getTicketTypes() {
        return ticketTypes;
    }

    public void setTicketTypes(List<TicketType> ticketTypes) {
        this.ticketTypes = ticketTypes;
    }

    
}
