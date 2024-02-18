package fr.istic.taa.jaxrs.dao.dao;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.dao.generic.EntityManagerHelper;
import fr.istic.taa.jaxrs.domain.Ticket;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.util.List;

public class TicketDao extends AbstractJpaDao<Ticket, String> {

    private Class<Ticket> clazz;
    protected EntityManager entityManager;
    public TicketDao() {
        this.entityManager = EntityManagerHelper.getEntityManager();
    }

    public void setClazz(Class<Ticket> clazzToSet) {
        this.clazz = clazzToSet;
    }
    @Override
    public void save(Ticket entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(entity); // Réattacher l'entité détachée
        transaction.commit();
    }

    @Override
    public Ticket update(Ticket entity) {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin(); // Début de la transaction

        try {
            Ticket updatedTicket = entityManager.merge(entity);
            tx.commit(); // Validation de la transaction
            return updatedTicket;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Annulation de la transaction en cas d'erreur
            }
            throw e; // Propagation de l'exception
        }
    }
    @Override
    public void delete(Ticket entity) {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin(); // Début de la transaction

        try {
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
            tx.commit(); // Validation de la transaction
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Annulation de la transaction en cas d'erreur
            }
            throw e; // Propagation de l'exception
        }
    }

    @Override
    public void deleteById(String entityId) {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin(); // Début de la transaction

        try {
            Ticket TicketToDelete = entityManager.find(Ticket.class, entityId);
            if (TicketToDelete != null) {
                entityManager.remove(TicketToDelete);
                tx.commit(); // Validation de la transaction
            } else {
                // Gérer le cas où l'entité avec l'ID donné n'existe pas
                // Vous pouvez lever une exception ou effectuer un traitement approprié
                // Dans cet exemple, je lève une IllegalArgumentException
                throw new IllegalArgumentException("Ticket with ID " + entityId + " not found");
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Annulation de la transaction en cas d'erreur
            }
            throw e; // Propagation de l'exception
        }
    }

    @Override
    public Ticket findOne(String id) {
        return entityManager.find(Ticket.class,id);
    }

    @Override
    public List<Ticket> findAll() {
        Query query = entityManager.createQuery("SELECT p FROM Ticket p", Ticket.class);
        return query.getResultList();
    }
}
