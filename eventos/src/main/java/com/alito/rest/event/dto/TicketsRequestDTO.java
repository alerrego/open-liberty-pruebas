package com.alito.rest.event.dto;

import java.util.List;

public class TicketsRequestDTO {
    List<TicketRequestDTO> tickets;

    public List<TicketRequestDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketRequestDTO> tickets) {
        this.tickets = tickets;
    }

    
}
