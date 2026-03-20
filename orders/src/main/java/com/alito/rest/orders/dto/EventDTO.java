package com.alito.rest.orders.dto;

import java.time.LocalDateTime;
import java.util.List;

public class EventDTO {
    private Long id;
    private String band;
    private String location;
    private LocalDateTime date;

    private List<TicketTypesDTO> ticketTypes;

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

    public List<TicketTypesDTO> getTicketTypes() {
        return ticketTypes;
    }

    public void setTicketTypes(List<TicketTypesDTO> ticketTypes) {
        this.ticketTypes = ticketTypes;
    }

    

}
