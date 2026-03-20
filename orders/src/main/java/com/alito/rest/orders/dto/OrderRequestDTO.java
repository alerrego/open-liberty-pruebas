package com.alito.rest.orders.dto;

import java.util.List;

public class OrderRequestDTO {
    private Long eventId;
    private List<TicketDTO> tickets;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public List<TicketDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDTO> tickets) {
        this.tickets = tickets;
    }

    
    
}
