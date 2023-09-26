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
import modelo.RecuperacionClave;
import modelo.Usuario;

/**
 *
 * @author luismorales
 */
public class RecuperacionClaveJpaController implements Serializable {

    public RecuperacionClaveJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RecuperacionClave recuperacionClave) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario idUsuario = recuperacionClave.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                recuperacionClave.setIdUsuario(idUsuario);
            }
            em.persist(recuperacionClave);
            if (idUsuario != null) {
                idUsuario.getRecuperacionClaveList().add(recuperacionClave);
                idUsuario = em.merge(idUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RecuperacionClave recuperacionClave) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RecuperacionClave persistentRecuperacionClave = em.find(RecuperacionClave.class, recuperacionClave.getIdRecuperacion());
            Usuario idUsuarioOld = persistentRecuperacionClave.getIdUsuario();
            Usuario idUsuarioNew = recuperacionClave.getIdUsuario();
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                recuperacionClave.setIdUsuario(idUsuarioNew);
            }
            recuperacionClave = em.merge(recuperacionClave);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getRecuperacionClaveList().remove(recuperacionClave);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getRecuperacionClaveList().add(recuperacionClave);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = recuperacionClave.getIdRecuperacion();
                if (findRecuperacionClave(id) == null) {
                    throw new NonexistentEntityException("The recuperacionClave with id " + id + " no longer exists.");
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
            RecuperacionClave recuperacionClave;
            try {
                recuperacionClave = em.getReference(RecuperacionClave.class, id);
                recuperacionClave.getIdRecuperacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recuperacionClave with id " + id + " no longer exists.", enfe);
            }
            Usuario idUsuario = recuperacionClave.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getRecuperacionClaveList().remove(recuperacionClave);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(recuperacionClave);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RecuperacionClave> findRecuperacionClaveEntities() {
        return findRecuperacionClaveEntities(true, -1, -1);
    }

    public List<RecuperacionClave> findRecuperacionClaveEntities(int maxResults, int firstResult) {
        return findRecuperacionClaveEntities(false, maxResults, firstResult);
    }

    private List<RecuperacionClave> findRecuperacionClaveEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RecuperacionClave.class));
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

    public RecuperacionClave findRecuperacionClave(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RecuperacionClave.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecuperacionClaveCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RecuperacionClave> rt = cq.from(RecuperacionClave.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
