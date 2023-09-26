/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Persona;
import modelo.TelefonoPersona;

/**
 *
 * @author luismorales
 */
public class TelefonoPersonaJpaController implements Serializable {

    public TelefonoPersonaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TelefonoPersona telefonoPersona) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona idPersona = telefonoPersona.getIdPersona();
            if (idPersona != null) {
                idPersona = em.getReference(idPersona.getClass(), idPersona.getIdPersona());
                telefonoPersona.setIdPersona(idPersona);
            }
            em.persist(telefonoPersona);
            if (idPersona != null) {
                idPersona.getTelefonoPersonaList().add(telefonoPersona);
                idPersona = em.merge(idPersona);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TelefonoPersona telefonoPersona) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TelefonoPersona persistentTelefonoPersona = em.find(TelefonoPersona.class, telefonoPersona.getIdTelefonoPersona());
            Persona idPersonaOld = persistentTelefonoPersona.getIdPersona();
            Persona idPersonaNew = telefonoPersona.getIdPersona();
            if (idPersonaNew != null) {
                idPersonaNew = em.getReference(idPersonaNew.getClass(), idPersonaNew.getIdPersona());
                telefonoPersona.setIdPersona(idPersonaNew);
            }
            telefonoPersona = em.merge(telefonoPersona);
            if (idPersonaOld != null && !idPersonaOld.equals(idPersonaNew)) {
                idPersonaOld.getTelefonoPersonaList().remove(telefonoPersona);
                idPersonaOld = em.merge(idPersonaOld);
            }
            if (idPersonaNew != null && !idPersonaNew.equals(idPersonaOld)) {
                idPersonaNew.getTelefonoPersonaList().add(telefonoPersona);
                idPersonaNew = em.merge(idPersonaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = telefonoPersona.getIdTelefonoPersona();
                if (findTelefonoPersona(id) == null) {
                    throw new NonexistentEntityException("The telefonoPersona with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TelefonoPersona telefonoPersona;
            try {
                telefonoPersona = em.getReference(TelefonoPersona.class, id);
                telefonoPersona.getIdTelefonoPersona();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The telefonoPersona with id " + id + " no longer exists.", enfe);
            }
            Persona idPersona = telefonoPersona.getIdPersona();
            if (idPersona != null) {
                idPersona.getTelefonoPersonaList().remove(telefonoPersona);
                idPersona = em.merge(idPersona);
            }
            em.remove(telefonoPersona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TelefonoPersona> findTelefonoPersonaEntities() {
        return findTelefonoPersonaEntities(true, -1, -1);
    }

    public List<TelefonoPersona> findTelefonoPersonaEntities(int maxResults, int firstResult) {
        return findTelefonoPersonaEntities(false, maxResults, firstResult);
    }

    private List<TelefonoPersona> findTelefonoPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TelefonoPersona.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public TelefonoPersona findTelefonoPersona(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TelefonoPersona.class, id);
        } finally {
            em.close();
        }
    }

    public int getTelefonoPersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TelefonoPersona> rt = cq.from(TelefonoPersona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
