package com.alito.rest.orders.repository;

import java.util.List;

import com.alito.rest.orders.model.Order;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class OrderRepository {
    @PersistenceContext(unitName="OrdersPU")
    private EntityManager em;

    @Transactional
    public void save(Order order) {
        em.persist(order);
    }

    // MAGIA SQL: VERIFICAMOS QUE NINGUN DNI DE LA COMPRA YA ESTE ASOCIADO AL EVENTO
    public boolean ifExistDniInTickets(Long eventId, List<String> dnis) {
        String jpql = "SELECT COUNT(t) FROM Ticket t WHERE t.eventId = :eventId AND t.dni IN :dnis";
        
        Long count = em.createQuery(jpql, Long.class)
                .setParameter("eventId", eventId)
                .setParameter("dnis", dnis)
                .getSingleResult();
                
        return count > 0; // SI ES MAYOR YA TENEMOS ALGUN DNI EN ESE EVENTO
    }

    // BUSCAMOS LAS ORDENES DE UN USUARIO ESPECIFICIO
    public List<Order> findByBuyerId(Long buyerId) {
        String jpql = "SELECT o FROM Order o WHERE o.buyerUserId = :buyerId ORDER BY o.purchaseDate DESC";
        
        return em.createQuery(jpql, Order.class)
                 .setParameter("buyerId", buyerId)
                 .getResultList();
    }

    public Order findById(Long id) {
        return em.find(Order.class, id);
    }
}
