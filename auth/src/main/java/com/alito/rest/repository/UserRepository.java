package com.alito.rest.repository;

import java.util.Optional;

import com.alito.rest.model.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserRepository {
    @PersistenceContext(unitName="AuthPU") //TIENE QUE COINCIDIR EL unitName con el puesto en el persistence.xml
    private EntityManager em; //TRADUCTOR ENTRE OBJ Y BD

    @Transactional
    public void save(User user){
        em.persist(user);
    }

    public Optional<User> findByEmail(String email){
        try {
            User u = em.createQuery("SELECT u FROM User u WHERE u.email = :email",User.class)
                .setParameter("email",email)
                .getSingleResult();
            return Optional.of(u);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
