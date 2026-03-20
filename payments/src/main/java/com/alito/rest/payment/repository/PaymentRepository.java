package com.alito.rest.payment.repository;

import com.alito.rest.payment.model.Payment;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PaymentRepository {

    @PersistenceContext(unitName = "PaymentsPU")
    private EntityManager em;

    @Transactional
    public void save(Payment payment) {
        em.persist(payment);
        em.flush();
    }

    @Transactional
    public void update(Payment payment) {
        em.merge(payment);
    }

    public Payment findById(Long id) {
        return em.find(Payment.class, id);
    }
}
