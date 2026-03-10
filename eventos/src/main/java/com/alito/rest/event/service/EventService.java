package com.alito.rest.event.service;

import java.util.List;

import com.alito.rest.event.dto.EventDTO;
import com.alito.rest.event.model.Event;
import com.alito.rest.event.repository.EventRepository;
import com.alito.rest.ticketType.dto.TicketTypeDTO;
import com.alito.rest.ticketType.model.TicketType;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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
}
