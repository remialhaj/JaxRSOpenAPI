package fr.istic.taa.jaxrs.dao.dao;

import fr.istic.taa.jaxrs.dao.generic.*;
import fr.istic.taa.jaxrs.domain.Comment;
import jakarta.persistence.*;

import java.util.List;

public class CommentDao extends AbstractJpaDao<Comment, String> {

    private Class<Comment> clazz;
    protected EntityManager entityManager;
    public CommentDao() {
        this.entityManager = EntityManagerHelper.getEntityManager();
    }

    public void setClazz(Class<Comment> clazzToSet) {
        this.clazz = clazzToSet;
    }
    @Override
    public void save(Comment entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(entity); // Réattacher l'entité détachée
        transaction.commit();
    }

    @Override
    public Comment update(Comment entity) {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin(); // Début de la transaction

        try {
            Comment updatedComment = entityManager.merge(entity);
            tx.commit(); // Validation de la transaction
            return updatedComment;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Annulation de la transaction en cas d'erreur
            }
            throw e; // Propagation de l'exception
        }
    }
    @Override
    public void delete(Comment entity) {
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
            Comment CommentToDelete = entityManager.find(Comment.class, entityId);
            if (CommentToDelete != null) {
                entityManager.remove(CommentToDelete);
                tx.commit(); // Validation de la transaction
            } else {
                // Gérer le cas où l'entité avec l'ID donné n'existe pas
                // Vous pouvez lever une exception ou effectuer un traitement approprié
                // Dans cet exemple, je lève une IllegalArgumentException
                throw new IllegalArgumentException("Comment with ID " + entityId + " not found");
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Annulation de la transaction en cas d'erreur
            }
            throw e; // Propagation de l'exception
        }
    }

    @Override
    public Comment findOne(String id) {
        return entityManager.find(Comment.class,id);
    }

    @Override
    public List<Comment> findAll() {
        Query query = entityManager.createQuery("SELECT p FROM Comment p", Comment.class);
        return query.getResultList();
    }
}
