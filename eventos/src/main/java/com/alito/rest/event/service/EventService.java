package com.alito.rest.event.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alito.rest.event.dto.EventDTO;
import com.alito.rest.event.dto.TicketRequestDTO;
import com.alito.rest.event.dto.TicketsRequestDTO;
import com.alito.rest.event.model.Event;
import com.alito.rest.event.repository.EventRepository;
import com.alito.rest.ticketType.dto.TicketTypeDTO;
import com.alito.rest.ticketType.model.TicketType;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class EventService {
    @Inject
    EventRepository eventRepository;

    public Event createEvent(EventDTO eventDTO){
        Event e = new Event();
        e.setBand(eventDTO.band);
        e.setDate(eventDTO.date);
        e.setLocation(eventDTO.location);
        
        if(eventDTO.ticketTypes != null){
            for (TicketTypeDTO ticketTypeDTO : eventDTO.ticketTypes){
                TicketType t = new TicketType();
                t.setName(ticketTypeDTO.name);
                t.setPrice(ticketTypeDTO.price);
                t.setStock(ticketTypeDTO.stock);
                e.addTicketType(t);
            }
        }

        return eventRepository.save(e);
    }

    public List<Event> getAllEvents(){
        return eventRepository.findAll();
    }

    public Event getEventById(Long id){
        Event e = eventRepository.findById(id);
        if(e == null){
            throw new WebApplicationException(
                Response.status(Response.Status.NOT_FOUND)
                .entity("Event with ID : "+id+" not found.")
                .build()
            );
        }
        return e;
    }

    @Transactional
    public void decreaseStock(Long eventId, TicketsRequestDTO tickets){
        Map<String,Long> quantityForTypes = tickets.getTickets().stream()
            .collect(Collectors.groupingBy(TicketRequestDTO::getTicketType,Collectors.counting()));

        for(Map.Entry<String,Long> ticket : quantityForTypes.entrySet()){
            boolean successfulDiscount = eventRepository.decreaseStock(eventId, ticket.getKey(), ticket.getValue());

            if(!successfulDiscount){
                throw new WebApplicationException(
                    Response.status(Response.Status.CONFLICT)
                            .entity("The stock is insufficient for the entry type: " + ticket.getKey())
                            .build()
                );
            }
        }
    }
}
