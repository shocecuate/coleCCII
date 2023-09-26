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
import modelo.Bimestre;
import modelo.Bimestreporcarrera;
import modelo.Carrera;

/**
 *
 * @author luismorales
 */
public class BimestreporcarreraJpaController implements Serializable {

    public BimestreporcarreraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Bimestreporcarrera bimestreporcarrera) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Bimestre idBimestre = bimestreporcarrera.getIdBimestre();
            if (idBimestre != null) {
                idBimestre = em.getReference(idBimestre.getClass(), idBimestre.getIdBimestre());
                bimestreporcarrera.setIdBimestre(idBimestre);
            }
            Carrera idCarrera = bimestreporcarrera.getIdCarrera();
            if (idCarrera != null) {
                idCarrera = em.getReference(idCarrera.getClass(), idCarrera.getIdCarrera());
                bimestreporcarrera.setIdCarrera(idCarrera);
            }
            em.persist(bimestreporcarrera);
            if (idBimestre != null) {
                idBimestre.getBimestreporcarreraList().add(bimestreporcarrera);
                idBimestre = em.merge(idBimestre);
            }
            if (idCarrera != null) {
                idCarrera.getBimestreporcarreraList().add(bimestreporcarrera);
                idCarrera = em.merge(idCarrera);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Bimestreporcarrera bimestreporcarrera) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Bimestreporcarrera persistentBimestreporcarrera = em.find(Bimestreporcarrera.class, bimestreporcarrera.getIdBimestreporcarrera());
            Bimestre idBimestreOld = persistentBimestreporcarrera.getIdBimestre();
            Bimestre idBimestreNew = bimestreporcarrera.getIdBimestre();
            Carrera idCarreraOld = persistentBimestreporcarrera.getIdCarrera();
            Carrera idCarreraNew = bimestreporcarrera.getIdCarrera();
            if (idBimestreNew != null) {
                idBimestreNew = em.getReference(idBimestreNew.getClass(), idBimestreNew.getIdBimestre());
                bimestreporcarrera.setIdBimestre(idBimestreNew);
            }
            if (idCarreraNew != null) {
                idCarreraNew = em.getReference(idCarreraNew.getClass(), idCarreraNew.getIdCarrera());
                bimestreporcarrera.setIdCarrera(idCarreraNew);
            }
            bimestreporcarrera = em.merge(bimestreporcarrera);
            if (idBimestreOld != null && !idBimestreOld.equals(idBimestreNew)) {
                idBimestreOld.getBimestreporcarreraList().remove(bimestreporcarrera);
                idBimestreOld = em.merge(idBimestreOld);
            }
            if (idBimestreNew != null && !idBimestreNew.equals(idBimestreOld)) {
                idBimestreNew.getBimestreporcarreraList().add(bimestreporcarrera);
                idBimestreNew = em.merge(idBimestreNew);
            }
            if (idCarreraOld != null && !idCarreraOld.equals(idCarreraNew)) {
                idCarreraOld.getBimestreporcarreraList().remove(bimestreporcarrera);
                idCarreraOld = em.merge(idCarreraOld);
            }
            if (idCarreraNew != null && !idCarreraNew.equals(idCarreraOld)) {
                idCarreraNew.getBimestreporcarreraList().add(bimestreporcarrera);
                idCarreraNew = em.merge(idCarreraNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = bimestreporcarrera.getIdBimestreporcarrera();
                if (findBimestreporcarrera(id) == null) {
                    throw new NonexistentEntityException("The bimestreporcarrera with id " + id + " no longer exists.");
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
            Bimestreporcarrera bimestreporcarrera;
            try {
                bimestreporcarrera = em.getReference(Bimestreporcarrera.class, id);
                bimestreporcarrera.getIdBimestreporcarrera();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bimestreporcarrera with id " + id + " no longer exists.", enfe);
            }
            Bimestre idBimestre = bimestreporcarrera.getIdBimestre();
            if (idBimestre != null) {
                idBimestre.getBimestreporcarreraList().remove(bimestreporcarrera);
                idBimestre = em.merge(idBimestre);
            }
            Carrera idCarrera = bimestreporcarrera.getIdCarrera();
            if (idCarrera != null) {
                idCarrera.getBimestreporcarreraList().remove(bimestreporcarrera);
                idCarrera = em.merge(idCarrera);
            }
            em.remove(bimestreporcarrera);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Bimestreporcarrera> findBimestreporcarreraEntities() {
        return findBimestreporcarreraEntities(true, -1, -1);
    }

    public List<Bimestreporcarrera> findBimestreporcarreraEntities(int maxResults, int firstResult) {
        return findBimestreporcarreraEntities(false, maxResults, firstResult);
    }

    private List<Bimestreporcarrera> findBimestreporcarreraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Bimestreporcarrera.class));
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

    public Bimestreporcarrera findBimestreporcarrera(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Bimestreporcarrera.class, id);
        } finally {
            em.close();
        }
    }

    public int getBimestreporcarreraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Bimestreporcarrera> rt = cq.from(Bimestreporcarrera.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
