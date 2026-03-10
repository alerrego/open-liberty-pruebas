package com.alito.rest.event.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.alito.rest.ticketType.dto.TicketTypeDTO;

public class EventDTO {
    public String band;
    public String location;
    public LocalDateTime date;
    public List<TicketTypeDTO> ticketTypes;
}
