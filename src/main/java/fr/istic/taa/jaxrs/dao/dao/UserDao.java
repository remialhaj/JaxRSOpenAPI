package fr.istic.taa.jaxrs.dao.dao;

import fr.istic.taa.jaxrs.dao.generic.*;
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
            User UserToDelete = entityManager.find(User.class, entityId);
            if (UserToDelete != null) {
                entityManager.remove(UserToDelete);
                tx.commit(); // Validation de la transaction
            } else {
                // Gérer le cas où l'entité avec l'ID donné n'existe pas
                // Vous pouvez lever une exception ou effectuer un traitement approprié
                // Dans cet exemple, je lève une IllegalArgumentException
                throw new IllegalArgumentException("User with ID " + entityId + " not found");
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Annulation de la transaction en cas d'erreur
            }
            throw e; // Propagation de l'exception
        }
    }

    @Override
    public User findOne(String id) {
        return entityManager.find(User.class,id);
    }

    @Override
    public List<User> findAll() {
        Query query = entityManager.createQuery("SELECT p FROM User p", User.class);
        return query.getResultList();
    }
}
