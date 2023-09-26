/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Alumno;
import modelo.Curso;
import modelo.Mensaje;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Tarea;

/**
 *
 * @author luismorales
 */
public class TareaJpaController implements Serializable {

    public TareaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tarea tarea) {
        if (tarea.getMensajeList() == null) {
            tarea.setMensajeList(new ArrayList<Mensaje>());
        }
        if (tarea.getMensajeList1() == null) {
            tarea.setMensajeList1(new ArrayList<Mensaje>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno idAlumno = tarea.getIdAlumno();
            if (idAlumno != null) {
                idAlumno = em.getReference(idAlumno.getClass(), idAlumno.getIdAlumno());
                tarea.setIdAlumno(idAlumno);
            }
            Curso idCurso = tarea.getIdCurso();
            if (idCurso != null) {
                idCurso = em.getReference(idCurso.getClass(), idCurso.getIdCurso());
                tarea.setIdCurso(idCurso);
            }
            List<Mensaje> attachedMensajeList = new ArrayList<Mensaje>();
            for (Mensaje mensajeListMensajeToAttach : tarea.getMensajeList()) {
                mensajeListMensajeToAttach = em.getReference(mensajeListMensajeToAttach.getClass(), mensajeListMensajeToAttach.getIdMensaje());
                attachedMensajeList.add(mensajeListMensajeToAttach);
            }
            tarea.setMensajeList(attachedMensajeList);
            List<Mensaje> attachedMensajeList1 = new ArrayList<Mensaje>();
            for (Mensaje mensajeList1MensajeToAttach : tarea.getMensajeList1()) {
                mensajeList1MensajeToAttach = em.getReference(mensajeList1MensajeToAttach.getClass(), mensajeList1MensajeToAttach.getIdMensaje());
                attachedMensajeList1.add(mensajeList1MensajeToAttach);
            }
            tarea.setMensajeList1(attachedMensajeList1);
            em.persist(tarea);
            if (idAlumno != null) {
                idAlumno.getTareaList().add(tarea);
                idAlumno = em.merge(idAlumno);
            }
            if (idCurso != null) {
                idCurso.getTareaList().add(tarea);
                idCurso = em.merge(idCurso);
            }
            for (Mensaje mensajeListMensaje : tarea.getMensajeList()) {
                mensajeListMensaje.getTareaList().add(tarea);
                mensajeListMensaje = em.merge(mensajeListMensaje);
            }
            for (Mensaje mensajeList1Mensaje : tarea.getMensajeList1()) {
                mensajeList1Mensaje.getTareaList1().add(tarea);
                mensajeList1Mensaje = em.merge(mensajeList1Mensaje);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tarea tarea) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarea persistentTarea = em.find(Tarea.class, tarea.getIdTarea());
            Alumno idAlumnoOld = persistentTarea.getIdAlumno();
            Alumno idAlumnoNew = tarea.getIdAlumno();
            Curso idCursoOld = persistentTarea.getIdCurso();
            Curso idCursoNew = tarea.getIdCurso();
            List<Mensaje> mensajeListOld = persistentTarea.getMensajeList();
            List<Mensaje> mensajeListNew = tarea.getMensajeList();
            List<Mensaje> mensajeList1Old = persistentTarea.getMensajeList1();
            List<Mensaje> mensajeList1New = tarea.getMensajeList1();
            if (idAlumnoNew != null) {
                idAlumnoNew = em.getReference(idAlumnoNew.getClass(), idAlumnoNew.getIdAlumno());
                tarea.setIdAlumno(idAlumnoNew);
            }
            if (idCursoNew != null) {
                idCursoNew = em.getReference(idCursoNew.getClass(), idCursoNew.getIdCurso());
                tarea.setIdCurso(idCursoNew);
            }
            List<Mensaje> attachedMensajeListNew = new ArrayList<Mensaje>();
            for (Mensaje mensajeListNewMensajeToAttach : mensajeListNew) {
                mensajeListNewMensajeToAttach = em.getReference(mensajeListNewMensajeToAttach.getClass(), mensajeListNewMensajeToAttach.getIdMensaje());
                attachedMensajeListNew.add(mensajeListNewMensajeToAttach);
            }
            mensajeListNew = attachedMensajeListNew;
            tarea.setMensajeList(mensajeListNew);
            List<Mensaje> attachedMensajeList1New = new ArrayList<Mensaje>();
            for (Mensaje mensajeList1NewMensajeToAttach : mensajeList1New) {
                mensajeList1NewMensajeToAttach = em.getReference(mensajeList1NewMensajeToAttach.getClass(), mensajeList1NewMensajeToAttach.getIdMensaje());
                attachedMensajeList1New.add(mensajeList1NewMensajeToAttach);
            }
            mensajeList1New = attachedMensajeList1New;
            tarea.setMensajeList1(mensajeList1New);
            tarea = em.merge(tarea);
            if (idAlumnoOld != null && !idAlumnoOld.equals(idAlumnoNew)) {
                idAlumnoOld.getTareaList().remove(tarea);
                idAlumnoOld = em.merge(idAlumnoOld);
            }
            if (idAlumnoNew != null && !idAlumnoNew.equals(idAlumnoOld)) {
                idAlumnoNew.getTareaList().add(tarea);
                idAlumnoNew = em.merge(idAlumnoNew);
            }
            if (idCursoOld != null && !idCursoOld.equals(idCursoNew)) {
                idCursoOld.getTareaList().remove(tarea);
                idCursoOld = em.merge(idCursoOld);
            }
            if (idCursoNew != null && !idCursoNew.equals(idCursoOld)) {
                idCursoNew.getTareaList().add(tarea);
                idCursoNew = em.merge(idCursoNew);
            }
            for (Mensaje mensajeListOldMensaje : mensajeListOld) {
                if (!mensajeListNew.contains(mensajeListOldMensaje)) {
                    mensajeListOldMensaje.getTareaList().remove(tarea);
                    mensajeListOldMensaje = em.merge(mensajeListOldMensaje);
                }
            }
            for (Mensaje mensajeListNewMensaje : mensajeListNew) {
                if (!mensajeListOld.contains(mensajeListNewMensaje)) {
                    mensajeListNewMensaje.getTareaList().add(tarea);
                    mensajeListNewMensaje = em.merge(mensajeListNewMensaje);
                }
            }
            for (Mensaje mensajeList1OldMensaje : mensajeList1Old) {
                if (!mensajeList1New.contains(mensajeList1OldMensaje)) {
                    mensajeList1OldMensaje.getTareaList1().remove(tarea);
                    mensajeList1OldMensaje = em.merge(mensajeList1OldMensaje);
                }
            }
            for (Mensaje mensajeList1NewMensaje : mensajeList1New) {
                if (!mensajeList1Old.contains(mensajeList1NewMensaje)) {
                    mensajeList1NewMensaje.getTareaList1().add(tarea);
                    mensajeList1NewMensaje = em.merge(mensajeList1NewMensaje);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tarea.getIdTarea();
                if (findTarea(id) == null) {
                    throw new NonexistentEntityException("The tarea with id " + id + " no longer exists.");
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
            Tarea tarea;
            try {
                tarea = em.getReference(Tarea.class, id);
                tarea.getIdTarea();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarea with id " + id + " no longer exists.", enfe);
            }
            Alumno idAlumno = tarea.getIdAlumno();
            if (idAlumno != null) {
                idAlumno.getTareaList().remove(tarea);
                idAlumno = em.merge(idAlumno);
            }
            Curso idCurso = tarea.getIdCurso();
            if (idCurso != null) {
                idCurso.getTareaList().remove(tarea);
                idCurso = em.merge(idCurso);
            }
            List<Mensaje> mensajeList = tarea.getMensajeList();
            for (Mensaje mensajeListMensaje : mensajeList) {
                mensajeListMensaje.getTareaList().remove(tarea);
                mensajeListMensaje = em.merge(mensajeListMensaje);
            }
            List<Mensaje> mensajeList1 = tarea.getMensajeList1();
            for (Mensaje mensajeList1Mensaje : mensajeList1) {
                mensajeList1Mensaje.getTareaList1().remove(tarea);
                mensajeList1Mensaje = em.merge(mensajeList1Mensaje);
            }
            em.remove(tarea);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tarea> findTareaEntities() {
        return findTareaEntities(true, -1, -1);
    }

    public List<Tarea> findTareaEntities(int maxResults, int firstResult) {
        return findTareaEntities(false, maxResults, firstResult);
    }

    private List<Tarea> findTareaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tarea.class));
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

    public Tarea findTarea(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarea.class, id);
        } finally {
            em.close();
        }
    }

    public int getTareaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tarea> rt = cq.from(Tarea.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
