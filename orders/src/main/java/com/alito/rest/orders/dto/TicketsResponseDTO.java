package com.alito.rest.orders.dto;

import java.util.List;

public class TicketsResponseDTO {
    List<TicketDTO> tickets;

    

    public TicketsResponseDTO(List<TicketDTO> tickets) {
        this.tickets = tickets;
    }

    public List<TicketDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDTO> tickets) {
        this.tickets = tickets;
    }

    
}