package fr.istic.taa.jaxrs.dao.dao;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.dao.generic.EntityManagerHelper;
import fr.istic.taa.jaxrs.domain.Admin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.util.List;

public class AdminDao extends AbstractJpaDao<Admin, String> {

    private Class<Admin> clazz;
    protected EntityManager entityManager;
    public AdminDao() {
        this.entityManager = EntityManagerHelper.getEntityManager();
    }

    public void setClazz(Class<Admin> clazzToSet) {
        this.clazz = clazzToSet;
    }
    @Override
    public void save(Admin entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(entity); // Réattacher l'entité détachée
        transaction.commit();
    }

    @Override
    public Admin update(Admin entity) {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin(); // Début de la transaction

        try {
            Admin updatedAdmin = entityManager.merge(entity);
            tx.commit(); // Validation de la transaction
            return updatedAdmin;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Annulation de la transaction en cas d'erreur
            }
            throw e; // Propagation de l'exception
        }
    }
    @Override
    public void delete(Admin entity) {
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
            Admin AdminToDelete = entityManager.find(Admin.class, entityId);
            if (AdminToDelete != null) {
                entityManager.remove(AdminToDelete);
                tx.commit(); // Validation de la transaction
            } else {
                // Gérer le cas où l'entité avec l'ID donné n'existe pas
                // Vous pouvez lever une exception ou effectuer un traitement approprié
                // Dans cet exemple, je lève une IllegalArgumentException
                throw new IllegalArgumentException("Admin with ID " + entityId + " not found");
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Annulation de la transaction en cas d'erreur
            }
            throw e; // Propagation de l'exception
        }
    }

    @Override
    public Admin findOne(String id) {
        return entityManager.find(Admin.class,id);
    }

    @Override
    public List<Admin> findAll() {
        Query query = entityManager.createQuery("SELECT p FROM Admin p", Admin.class);
        return query.getResultList();
    }
}
