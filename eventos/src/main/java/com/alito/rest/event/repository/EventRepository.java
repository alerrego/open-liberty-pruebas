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

    public Event findById(Long id){
        return em.find(Event.class, id);
    }

    @Transactional
    public boolean decreaseStock(Long eventId, String nameTicketType, Long quantityTickets){
        String jpql = "UPDATE TicketType t SET t.stock = t.stock - :quantity " +
                      "WHERE t.event.id = :eventId AND t.name = :name AND t.stock >= :quantity";

        int rowsModified = em.createQuery(jpql)
            .setParameter("quantity",quantityTickets)
            .setParameter("eventId",eventId)
            .setParameter("name",nameTicketType)
            .executeUpdate();

        return rowsModified > 0;
    }
}
