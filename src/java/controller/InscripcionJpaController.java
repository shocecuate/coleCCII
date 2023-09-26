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
import modelo.Cursoporcarrera;
import modelo.Inscripcion;
import modelo.Persona;
import modelo.Tipodepago;

/**
 *
 * @author luismorales
 */
public class InscripcionJpaController implements Serializable {

    public InscripcionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Inscripcion inscripcion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cursoporcarrera idCursoporCarrera = inscripcion.getIdCursoporCarrera();
            if (idCursoporCarrera != null) {
                idCursoporCarrera = em.getReference(idCursoporCarrera.getClass(), idCursoporCarrera.getIdCursoPorCarrera());
                inscripcion.setIdCursoporCarrera(idCursoporCarrera);
            }
            Persona idPersona = inscripcion.getIdPersona();
            if (idPersona != null) {
                idPersona = em.getReference(idPersona.getClass(), idPersona.getIdPersona());
                inscripcion.setIdPersona(idPersona);
            }
            Tipodepago idTipoPago = inscripcion.getIdTipoPago();
            if (idTipoPago != null) {
                idTipoPago = em.getReference(idTipoPago.getClass(), idTipoPago.getIdTipoPago());
                inscripcion.setIdTipoPago(idTipoPago);
            }
            em.persist(inscripcion);
            if (idCursoporCarrera != null) {
                idCursoporCarrera.getInscripcionList().add(inscripcion);
                idCursoporCarrera = em.merge(idCursoporCarrera);
            }
            if (idPersona != null) {
                idPersona.getInscripcionList().add(inscripcion);
                idPersona = em.merge(idPersona);
            }
            if (idTipoPago != null) {
                idTipoPago.getInscripcionList().add(inscripcion);
                idTipoPago = em.merge(idTipoPago);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Inscripcion inscripcion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Inscripcion persistentInscripcion = em.find(Inscripcion.class, inscripcion.getIdInscripcion());
            Cursoporcarrera idCursoporCarreraOld = persistentInscripcion.getIdCursoporCarrera();
            Cursoporcarrera idCursoporCarreraNew = inscripcion.getIdCursoporCarrera();
            Persona idPersonaOld = persistentInscripcion.getIdPersona();
            Persona idPersonaNew = inscripcion.getIdPersona();
            Tipodepago idTipoPagoOld = persistentInscripcion.getIdTipoPago();
            Tipodepago idTipoPagoNew = inscripcion.getIdTipoPago();
            if (idCursoporCarreraNew != null) {
                idCursoporCarreraNew = em.getReference(idCursoporCarreraNew.getClass(), idCursoporCarreraNew.getIdCursoPorCarrera());
                inscripcion.setIdCursoporCarrera(idCursoporCarreraNew);
            }
            if (idPersonaNew != null) {
                idPersonaNew = em.getReference(idPersonaNew.getClass(), idPersonaNew.getIdPersona());
                inscripcion.setIdPersona(idPersonaNew);
            }
            if (idTipoPagoNew != null) {
                idTipoPagoNew = em.getReference(idTipoPagoNew.getClass(), idTipoPagoNew.getIdTipoPago());
                inscripcion.setIdTipoPago(idTipoPagoNew);
            }
            inscripcion = em.merge(inscripcion);
            if (idCursoporCarreraOld != null && !idCursoporCarreraOld.equals(idCursoporCarreraNew)) {
                idCursoporCarreraOld.getInscripcionList().remove(inscripcion);
                idCursoporCarreraOld = em.merge(idCursoporCarreraOld);
            }
            if (idCursoporCarreraNew != null && !idCursoporCarreraNew.equals(idCursoporCarreraOld)) {
                idCursoporCarreraNew.getInscripcionList().add(inscripcion);
                idCursoporCarreraNew = em.merge(idCursoporCarreraNew);
            }
            if (idPersonaOld != null && !idPersonaOld.equals(idPersonaNew)) {
                idPersonaOld.getInscripcionList().remove(inscripcion);
                idPersonaOld = em.merge(idPersonaOld);
            }
            if (idPersonaNew != null && !idPersonaNew.equals(idPersonaOld)) {
                idPersonaNew.getInscripcionList().add(inscripcion);
                idPersonaNew = em.merge(idPersonaNew);
            }
            if (idTipoPagoOld != null && !idTipoPagoOld.equals(idTipoPagoNew)) {
                idTipoPagoOld.getInscripcionList().remove(inscripcion);
                idTipoPagoOld = em.merge(idTipoPagoOld);
            }
            if (idTipoPagoNew != null && !idTipoPagoNew.equals(idTipoPagoOld)) {
                idTipoPagoNew.getInscripcionList().add(inscripcion);
                idTipoPagoNew = em.merge(idTipoPagoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = inscripcion.getIdInscripcion();
                if (findInscripcion(id) == null) {
                    throw new NonexistentEntityException("The inscripcion with id " + id + " no longer exists.");
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
            Inscripcion inscripcion;
            try {
                inscripcion = em.getReference(Inscripcion.class, id);
                inscripcion.getIdInscripcion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The inscripcion with id " + id + " no longer exists.", enfe);
            }
            Cursoporcarrera idCursoporCarrera = inscripcion.getIdCursoporCarrera();
            if (idCursoporCarrera != null) {
                idCursoporCarrera.getInscripcionList().remove(inscripcion);
                idCursoporCarrera = em.merge(idCursoporCarrera);
            }
            Persona idPersona = inscripcion.getIdPersona();
            if (idPersona != null) {
                idPersona.getInscripcionList().remove(inscripcion);
                idPersona = em.merge(idPersona);
            }
            Tipodepago idTipoPago = inscripcion.getIdTipoPago();
            if (idTipoPago != null) {
                idTipoPago.getInscripcionList().remove(inscripcion);
                idTipoPago = em.merge(idTipoPago);
            }
            em.remove(inscripcion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Inscripcion> findInscripcionEntities() {
        return findInscripcionEntities(true, -1, -1);
    }

    public List<Inscripcion> findInscripcionEntities(int maxResults, int firstResult) {
        return findInscripcionEntities(false, maxResults, firstResult);
    }

    private List<Inscripcion> findInscripcionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Inscripcion.class));
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

    public Inscripcion findInscripcion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Inscripcion.class, id);
        } finally {
            em.close();
        }
    }

    public int getInscripcionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Inscripcion> rt = cq.from(Inscripcion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
