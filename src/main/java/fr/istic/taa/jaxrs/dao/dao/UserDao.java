package fr.istic.taa.jaxrs.dao.dao;

import fr.istic.taa.jaxrs.dao.generic.*;
import fr.istic.taa.jaxrs.domain.Comment;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.domain.User;
import jakarta.persistence.*;

import java.util.List;

public class UserDao extends AbstractJpaDao<User, String> {

    private Class<User> clazz;
    protected EntityManager entityManager;
    public UserDao() {
        this.entityManager = EntityManagerHelper.getEntityManager();
    }

    public void setClazz(Class<User> clazzToSet) {
        this.clazz = clazzToSet;
    }
    @Override
    public void save(User entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(entity); // Réattacher l'entité détachée
        transaction.commit();
    }

    @Override
    public User update(User entity) {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin(); // Début de la transaction

        try {
            User updatedUser = entityManager.merge(entity);
            tx.commit(); // Validation de la transaction
            return updatedUser;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Annulation de la transaction en cas d'erreur
            }
            throw e; // Propagation de l'exception
        }
    }
    @Override
    public void delete(User entity) {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin(); // Début de la transaction

        try {
            // Récupérer tous les commentaires associés à l'utilisateur
            List<Comment> comments = findCommentsByUser(entity);
            for (Comment comment : comments) {
                entityManager.remove(comment);
            }
            // Récupérer tous les tickets associés à l'utilisateur
            List<Ticket> tickets = findTicketsByUser(entity);
            for (Ticket ticket : tickets) {
                entityManager.remove(ticket);
            }

            try {
                entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
            } catch (PersistenceException e) {
                // Handle constraint violation
                // Log the error, notify the user, or take other appropriate actions
                // For example:
                System.err.println("Could not delete user due to associated comments.");
            }

            tx.commit(); // Validation de la transaction
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Annulation de la transaction en cas d'erreur
            }
            throw e; // Propagation de l'exception
        }
    }

    private List<Ticket> findTicketsByUser(User user) {
        Query query = entityManager.createQuery("SELECT t FROM Ticket t WHERE t.createdBy = :user OR t.assignedTo = :user", Ticket.class);
        query.setParameter("user", user);
        return query.getResultList();
    }

    private List<Comment> findCommentsByUser(User user) {
        Query query = entityManager.createQuery("SELECT c FROM Comment c WHERE c.user = :user", Comment.class);
        query.setParameter("user", user);
        return query.getResultList();
    }



    @Override
    public User findOne(String id) {
        return entityManager.find(User.class,id);
    }

    public User findByEmail(String email) {
        try {
            Query query = entityManager.createQuery("SELECT p FROM User p WHERE p.email = :email", User.class);
            query.setParameter("email", email);
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User findByUsername(String username) {
        try {
            Query query = entityManager.createQuery("SELECT p FROM User p WHERE p.username = :username", User.class);
            query.setParameter("username", username);
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<User> findAll() {
        Query query = entityManager.createQuery("SELECT p FROM User p", User.class);
        return query.getResultList();
    }
}
