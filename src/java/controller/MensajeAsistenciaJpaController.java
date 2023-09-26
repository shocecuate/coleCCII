/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Mensaje;
import modelo.Horario;
import modelo.MensajeAsistencia;
import modelo.MensajeAsistenciaPK;

/**
 *
 * @author luismorales
 */
public class MensajeAsistenciaJpaController implements Serializable {

    public MensajeAsistenciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MensajeAsistencia mensajeAsistencia) throws PreexistingEntityException, Exception {
        if (mensajeAsistencia.getMensajeAsistenciaPK() == null) {
            mensajeAsistencia.setMensajeAsistenciaPK(new MensajeAsistenciaPK());
        }
        mensajeAsistencia.getMensajeAsistenciaPK().setIdMensaje(mensajeAsistencia.getMensaje().getIdMensaje());
        mensajeAsistencia.getMensajeAsistenciaPK().setIdHorario(mensajeAsistencia.getHorario().getIdHorario());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mensaje mensaje = mensajeAsistencia.getMensaje();
            if (mensaje != null) {
                mensaje = em.getReference(mensaje.getClass(), mensaje.getIdMensaje());
                mensajeAsistencia.setMensaje(mensaje);
            }
            Horario horario = mensajeAsistencia.getHorario();
            if (horario != null) {
                horario = em.getReference(horario.getClass(), horario.getIdHorario());
                mensajeAsistencia.setHorario(horario);
            }
            em.persist(mensajeAsistencia);
            if (mensaje != null) {
                mensaje.getMensajeAsistenciaList().add(mensajeAsistencia);
                mensaje = em.merge(mensaje);
            }
            if (horario != null) {
                horario.getMensajeAsistenciaList().add(mensajeAsistencia);
                horario = em.merge(horario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMensajeAsistencia(mensajeAsistencia.getMensajeAsistenciaPK()) != null) {
                throw new PreexistingEntityException("MensajeAsistencia " + mensajeAsistencia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MensajeAsistencia mensajeAsistencia) throws NonexistentEntityException, Exception {
        mensajeAsistencia.getMensajeAsistenciaPK().setIdMensaje(mensajeAsistencia.getMensaje().getIdMensaje());
        mensajeAsistencia.getMensajeAsistenciaPK().setIdHorario(mensajeAsistencia.getHorario().getIdHorario());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MensajeAsistencia persistentMensajeAsistencia = em.find(MensajeAsistencia.class, mensajeAsistencia.getMensajeAsistenciaPK());
            Mensaje mensajeOld = persistentMensajeAsistencia.getMensaje();
            Mensaje mensajeNew = mensajeAsistencia.getMensaje();
            Horario horarioOld = persistentMensajeAsistencia.getHorario();
            Horario horarioNew = mensajeAsistencia.getHorario();
            if (mensajeNew != null) {
                mensajeNew = em.getReference(mensajeNew.getClass(), mensajeNew.getIdMensaje());
                mensajeAsistencia.setMensaje(mensajeNew);
            }
            if (horarioNew != null) {
                horarioNew = em.getReference(horarioNew.getClass(), horarioNew.getIdHorario());
                mensajeAsistencia.setHorario(horarioNew);
            }
            mensajeAsistencia = em.merge(mensajeAsistencia);
            if (mensajeOld != null && !mensajeOld.equals(mensajeNew)) {
                mensajeOld.getMensajeAsistenciaList().remove(mensajeAsistencia);
                mensajeOld = em.merge(mensajeOld);
            }
            if (mensajeNew != null && !mensajeNew.equals(mensajeOld)) {
                mensajeNew.getMensajeAsistenciaList().add(mensajeAsistencia);
                mensajeNew = em.merge(mensajeNew);
            }
            if (horarioOld != null && !horarioOld.equals(horarioNew)) {
                horarioOld.getMensajeAsistenciaList().remove(mensajeAsistencia);
                horarioOld = em.merge(horarioOld);
            }
            if (horarioNew != null && !horarioNew.equals(horarioOld)) {
                horarioNew.getMensajeAsistenciaList().add(mensajeAsistencia);
                horarioNew = em.merge(horarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MensajeAsistenciaPK id = mensajeAsistencia.getMensajeAsistenciaPK();
                if (findMensajeAsistencia(id) == null) {
                    throw new NonexistentEntityException("The mensajeAsistencia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MensajeAsistenciaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MensajeAsistencia mensajeAsistencia;
            try {
                mensajeAsistencia = em.getReference(MensajeAsistencia.class, id);
                mensajeAsistencia.getMensajeAsistenciaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mensajeAsistencia with id " + id + " no longer exists.", enfe);
            }
            Mensaje mensaje = mensajeAsistencia.getMensaje();
            if (mensaje != null) {
                mensaje.getMensajeAsistenciaList().remove(mensajeAsistencia);
                mensaje = em.merge(mensaje);
            }
            Horario horario = mensajeAsistencia.getHorario();
            if (horario != null) {
                horario.getMensajeAsistenciaList().remove(mensajeAsistencia);
                horario = em.merge(horario);
            }
            em.remove(mensajeAsistencia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MensajeAsistencia> findMensajeAsistenciaEntities() {
        return findMensajeAsistenciaEntities(true, -1, -1);
    }

    public List<MensajeAsistencia> findMensajeAsistenciaEntities(int maxResults, int firstResult) {
        return findMensajeAsistenciaEntities(false, maxResults, firstResult);
    }

    private List<MensajeAsistencia> findMensajeAsistenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MensajeAsistencia.class));
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

    public MensajeAsistencia findMensajeAsistencia(MensajeAsistenciaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MensajeAsistencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getMensajeAsistenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MensajeAsistencia> rt = cq.from(MensajeAsistencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
