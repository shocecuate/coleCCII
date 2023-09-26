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
import modelo.Asignacion;
import modelo.Horario;
import modelo.Profesor;

/**
 *
 * @author luismorales
 */
public class AsignacionJpaController implements Serializable {

    public AsignacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Asignacion asignacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Horario idHorario = asignacion.getIdHorario();
            if (idHorario != null) {
                idHorario = em.getReference(idHorario.getClass(), idHorario.getIdHorario());
                asignacion.setIdHorario(idHorario);
            }
            Profesor idProfesor = asignacion.getIdProfesor();
            if (idProfesor != null) {
                idProfesor = em.getReference(idProfesor.getClass(), idProfesor.getIdProfesor());
                asignacion.setIdProfesor(idProfesor);
            }
            em.persist(asignacion);
            if (idHorario != null) {
                idHorario.getAsignacionList().add(asignacion);
                idHorario = em.merge(idHorario);
            }
            if (idProfesor != null) {
                idProfesor.getAsignacionList().add(asignacion);
                idProfesor = em.merge(idProfesor);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Asignacion asignacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asignacion persistentAsignacion = em.find(Asignacion.class, asignacion.getIdAsignacion());
            Horario idHorarioOld = persistentAsignacion.getIdHorario();
            Horario idHorarioNew = asignacion.getIdHorario();
            Profesor idProfesorOld = persistentAsignacion.getIdProfesor();
            Profesor idProfesorNew = asignacion.getIdProfesor();
            if (idHorarioNew != null) {
                idHorarioNew = em.getReference(idHorarioNew.getClass(), idHorarioNew.getIdHorario());
                asignacion.setIdHorario(idHorarioNew);
            }
            if (idProfesorNew != null) {
                idProfesorNew = em.getReference(idProfesorNew.getClass(), idProfesorNew.getIdProfesor());
                asignacion.setIdProfesor(idProfesorNew);
            }
            asignacion = em.merge(asignacion);
            if (idHorarioOld != null && !idHorarioOld.equals(idHorarioNew)) {
                idHorarioOld.getAsignacionList().remove(asignacion);
                idHorarioOld = em.merge(idHorarioOld);
            }
            if (idHorarioNew != null && !idHorarioNew.equals(idHorarioOld)) {
                idHorarioNew.getAsignacionList().add(asignacion);
                idHorarioNew = em.merge(idHorarioNew);
            }
            if (idProfesorOld != null && !idProfesorOld.equals(idProfesorNew)) {
                idProfesorOld.getAsignacionList().remove(asignacion);
                idProfesorOld = em.merge(idProfesorOld);
            }
            if (idProfesorNew != null && !idProfesorNew.equals(idProfesorOld)) {
                idProfesorNew.getAsignacionList().add(asignacion);
                idProfesorNew = em.merge(idProfesorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = asignacion.getIdAsignacion();
                if (findAsignacion(id) == null) {
                    throw new NonexistentEntityException("The asignacion with id " + id + " no longer exists.");
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
            Asignacion asignacion;
            try {
                asignacion = em.getReference(Asignacion.class, id);
                asignacion.getIdAsignacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The asignacion with id " + id + " no longer exists.", enfe);
            }
            Horario idHorario = asignacion.getIdHorario();
            if (idHorario != null) {
                idHorario.getAsignacionList().remove(asignacion);
                idHorario = em.merge(idHorario);
            }
            Profesor idProfesor = asignacion.getIdProfesor();
            if (idProfesor != null) {
                idProfesor.getAsignacionList().remove(asignacion);
                idProfesor = em.merge(idProfesor);
            }
            em.remove(asignacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Asignacion> findAsignacionEntities() {
        return findAsignacionEntities(true, -1, -1);
    }

    public List<Asignacion> findAsignacionEntities(int maxResults, int firstResult) {
        return findAsignacionEntities(false, maxResults, firstResult);
    }

    private List<Asignacion> findAsignacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Asignacion.class));
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

    public Asignacion findAsignacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Asignacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getAsignacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Asignacion> rt = cq.from(Asignacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
