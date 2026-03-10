package com.alito.rest.event.repository;

import java.util.List;

import com.alito.rest.event.model.Event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class EventRepository {
    @PersistenceContext(unitName="EventosPU")
    private EntityManager em;

    @Transactional
    public Event save(Event evento) {
        if (evento.getId() == null) {
            em.persist(evento);
            return evento;
        }
        return em.merge(evento);
    }

    public List<Event> findAll() {
        return em.createQuery("SELECT e FROM Event e LEFT JOIN FETCH e.ticketTypes", Event.class).getResultList();
    }
}
