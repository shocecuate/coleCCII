/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Persona;
import modelo.Carrera;
import modelo.Tarea;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Alumno;
import modelo.Nota;
import modelo.Examen;
import modelo.Pagos;

/**
 *
 * @author luismorales
 */
public class AlumnoJpaController implements Serializable {

    public AlumnoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Alumno alumno) {
        if (alumno.getTareaList() == null) {
            alumno.setTareaList(new ArrayList<Tarea>());
        }
        if (alumno.getNotaList() == null) {
            alumno.setNotaList(new ArrayList<Nota>());
        }
        if (alumno.getExamenList() == null) {
            alumno.setExamenList(new ArrayList<Examen>());
        }
        if (alumno.getPagosList() == null) {
            alumno.setPagosList(new ArrayList<Pagos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona idPersona = alumno.getIdPersona();
            if (idPersona != null) {
                idPersona = em.getReference(idPersona.getClass(), idPersona.getIdPersona());
                alumno.setIdPersona(idPersona);
            }
            Carrera idCarrera = alumno.getIdCarrera();
            if (idCarrera != null) {
                idCarrera = em.getReference(idCarrera.getClass(), idCarrera.getIdCarrera());
                alumno.setIdCarrera(idCarrera);
            }
            List<Tarea> attachedTareaList = new ArrayList<Tarea>();
            for (Tarea tareaListTareaToAttach : alumno.getTareaList()) {
                tareaListTareaToAttach = em.getReference(tareaListTareaToAttach.getClass(), tareaListTareaToAttach.getIdTarea());
                attachedTareaList.add(tareaListTareaToAttach);
            }
            alumno.setTareaList(attachedTareaList);
            List<Nota> attachedNotaList = new ArrayList<Nota>();
            for (Nota notaListNotaToAttach : alumno.getNotaList()) {
                notaListNotaToAttach = em.getReference(notaListNotaToAttach.getClass(), notaListNotaToAttach.getIdNota());
                attachedNotaList.add(notaListNotaToAttach);
            }
            alumno.setNotaList(attachedNotaList);
            List<Examen> attachedExamenList = new ArrayList<Examen>();
            for (Examen examenListExamenToAttach : alumno.getExamenList()) {
                examenListExamenToAttach = em.getReference(examenListExamenToAttach.getClass(), examenListExamenToAttach.getIdExamen());
                attachedExamenList.add(examenListExamenToAttach);
            }
            alumno.setExamenList(attachedExamenList);
            List<Pagos> attachedPagosList = new ArrayList<Pagos>();
            for (Pagos pagosListPagosToAttach : alumno.getPagosList()) {
                pagosListPagosToAttach = em.getReference(pagosListPagosToAttach.getClass(), pagosListPagosToAttach.getIdPago());
                attachedPagosList.add(pagosListPagosToAttach);
            }
            alumno.setPagosList(attachedPagosList);
            em.persist(alumno);
            if (idPersona != null) {
                idPersona.getAlumnoList().add(alumno);
                idPersona = em.merge(idPersona);
            }
            if (idCarrera != null) {
                idCarrera.getAlumnoList().add(alumno);
                idCarrera = em.merge(idCarrera);
            }
            for (Tarea tareaListTarea : alumno.getTareaList()) {
                Alumno oldIdAlumnoOfTareaListTarea = tareaListTarea.getIdAlumno();
                tareaListTarea.setIdAlumno(alumno);
                tareaListTarea = em.merge(tareaListTarea);
                if (oldIdAlumnoOfTareaListTarea != null) {
                    oldIdAlumnoOfTareaListTarea.getTareaList().remove(tareaListTarea);
                    oldIdAlumnoOfTareaListTarea = em.merge(oldIdAlumnoOfTareaListTarea);
                }
            }
            for (Nota notaListNota : alumno.getNotaList()) {
                Alumno oldIdAlumnoOfNotaListNota = notaListNota.getIdAlumno();
                notaListNota.setIdAlumno(alumno);
                notaListNota = em.merge(notaListNota);
                if (oldIdAlumnoOfNotaListNota != null) {
                    oldIdAlumnoOfNotaListNota.getNotaList().remove(notaListNota);
                    oldIdAlumnoOfNotaListNota = em.merge(oldIdAlumnoOfNotaListNota);
                }
            }
            for (Examen examenListExamen : alumno.getExamenList()) {
                Alumno oldIdAlumnoOfExamenListExamen = examenListExamen.getIdAlumno();
                examenListExamen.setIdAlumno(alumno);
                examenListExamen = em.merge(examenListExamen);
                if (oldIdAlumnoOfExamenListExamen != null) {
                    oldIdAlumnoOfExamenListExamen.getExamenList().remove(examenListExamen);
                    oldIdAlumnoOfExamenListExamen = em.merge(oldIdAlumnoOfExamenListExamen);
                }
            }
            for (Pagos pagosListPagos : alumno.getPagosList()) {
                Alumno oldIdAlumnoOfPagosListPagos = pagosListPagos.getIdAlumno();
                pagosListPagos.setIdAlumno(alumno);
                pagosListPagos = em.merge(pagosListPagos);
                if (oldIdAlumnoOfPagosListPagos != null) {
                    oldIdAlumnoOfPagosListPagos.getPagosList().remove(pagosListPagos);
                    oldIdAlumnoOfPagosListPagos = em.merge(oldIdAlumnoOfPagosListPagos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Alumno alumno) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno persistentAlumno = em.find(Alumno.class, alumno.getIdAlumno());
            Persona idPersonaOld = persistentAlumno.getIdPersona();
            Persona idPersonaNew = alumno.getIdPersona();
            Carrera idCarreraOld = persistentAlumno.getIdCarrera();
            Carrera idCarreraNew = alumno.getIdCarrera();
            List<Tarea> tareaListOld = persistentAlumno.getTareaList();
            List<Tarea> tareaListNew = alumno.getTareaList();
            List<Nota> notaListOld = persistentAlumno.getNotaList();
            List<Nota> notaListNew = alumno.getNotaList();
            List<Examen> examenListOld = persistentAlumno.getExamenList();
            List<Examen> examenListNew = alumno.getExamenList();
            List<Pagos> pagosListOld = persistentAlumno.getPagosList();
            List<Pagos> pagosListNew = alumno.getPagosList();
            List<String> illegalOrphanMessages = null;
            for (Tarea tareaListOldTarea : tareaListOld) {
                if (!tareaListNew.contains(tareaListOldTarea)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarea " + tareaListOldTarea + " since its idAlumno field is not nullable.");
                }
            }
            for (Nota notaListOldNota : notaListOld) {
                if (!notaListNew.contains(notaListOldNota)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Nota " + notaListOldNota + " since its idAlumno field is not nullable.");
                }
            }
            for (Examen examenListOldExamen : examenListOld) {
                if (!examenListNew.contains(examenListOldExamen)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Examen " + examenListOldExamen + " since its idAlumno field is not nullable.");
                }
            }
            for (Pagos pagosListOldPagos : pagosListOld) {
                if (!pagosListNew.contains(pagosListOldPagos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pagos " + pagosListOldPagos + " since its idAlumno field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPersonaNew != null) {
                idPersonaNew = em.getReference(idPersonaNew.getClass(), idPersonaNew.getIdPersona());
                alumno.setIdPersona(idPersonaNew);
            }
            if (idCarreraNew != null) {
                idCarreraNew = em.getReference(idCarreraNew.getClass(), idCarreraNew.getIdCarrera());
                alumno.setIdCarrera(idCarreraNew);
            }
            List<Tarea> attachedTareaListNew = new ArrayList<Tarea>();
            for (Tarea tareaListNewTareaToAttach : tareaListNew) {
                tareaListNewTareaToAttach = em.getReference(tareaListNewTareaToAttach.getClass(), tareaListNewTareaToAttach.getIdTarea());
                attachedTareaListNew.add(tareaListNewTareaToAttach);
            }
            tareaListNew = attachedTareaListNew;
            alumno.setTareaList(tareaListNew);
            List<Nota> attachedNotaListNew = new ArrayList<Nota>();
            for (Nota notaListNewNotaToAttach : notaListNew) {
                notaListNewNotaToAttach = em.getReference(notaListNewNotaToAttach.getClass(), notaListNewNotaToAttach.getIdNota());
                attachedNotaListNew.add(notaListNewNotaToAttach);
            }
            notaListNew = attachedNotaListNew;
            alumno.setNotaList(notaListNew);
            List<Examen> attachedExamenListNew = new ArrayList<Examen>();
            for (Examen examenListNewExamenToAttach : examenListNew) {
                examenListNewExamenToAttach = em.getReference(examenListNewExamenToAttach.getClass(), examenListNewExamenToAttach.getIdExamen());
                attachedExamenListNew.add(examenListNewExamenToAttach);
            }
            examenListNew = attachedExamenListNew;
            alumno.setExamenList(examenListNew);
            List<Pagos> attachedPagosListNew = new ArrayList<Pagos>();
            for (Pagos pagosListNewPagosToAttach : pagosListNew) {
                pagosListNewPagosToAttach = em.getReference(pagosListNewPagosToAttach.getClass(), pagosListNewPagosToAttach.getIdPago());
                attachedPagosListNew.add(pagosListNewPagosToAttach);
            }
            pagosListNew = attachedPagosListNew;
            alumno.setPagosList(pagosListNew);
            alumno = em.merge(alumno);
            if (idPersonaOld != null && !idPersonaOld.equals(idPersonaNew)) {
                idPersonaOld.getAlumnoList().remove(alumno);
                idPersonaOld = em.merge(idPersonaOld);
            }
            if (idPersonaNew != null && !idPersonaNew.equals(idPersonaOld)) {
                idPersonaNew.getAlumnoList().add(alumno);
                idPersonaNew = em.merge(idPersonaNew);
            }
            if (idCarreraOld != null && !idCarreraOld.equals(idCarreraNew)) {
                idCarreraOld.getAlumnoList().remove(alumno);
                idCarreraOld = em.merge(idCarreraOld);
            }
            if (idCarreraNew != null && !idCarreraNew.equals(idCarreraOld)) {
                idCarreraNew.getAlumnoList().add(alumno);
                idCarreraNew = em.merge(idCarreraNew);
            }
            for (Tarea tareaListNewTarea : tareaListNew) {
                if (!tareaListOld.contains(tareaListNewTarea)) {
                    Alumno oldIdAlumnoOfTareaListNewTarea = tareaListNewTarea.getIdAlumno();
                    tareaListNewTarea.setIdAlumno(alumno);
                    tareaListNewTarea = em.merge(tareaListNewTarea);
                    if (oldIdAlumnoOfTareaListNewTarea != null && !oldIdAlumnoOfTareaListNewTarea.equals(alumno)) {
                        oldIdAlumnoOfTareaListNewTarea.getTareaList().remove(tareaListNewTarea);
                        oldIdAlumnoOfTareaListNewTarea = em.merge(oldIdAlumnoOfTareaListNewTarea);
                    }
                }
            }
            for (Nota notaListNewNota : notaListNew) {
                if (!notaListOld.contains(notaListNewNota)) {
                    Alumno oldIdAlumnoOfNotaListNewNota = notaListNewNota.getIdAlumno();
                    notaListNewNota.setIdAlumno(alumno);
                    notaListNewNota = em.merge(notaListNewNota);
                    if (oldIdAlumnoOfNotaListNewNota != null && !oldIdAlumnoOfNotaListNewNota.equals(alumno)) {
                        oldIdAlumnoOfNotaListNewNota.getNotaList().remove(notaListNewNota);
                        oldIdAlumnoOfNotaListNewNota = em.merge(oldIdAlumnoOfNotaListNewNota);
                    }
                }
            }
            for (Examen examenListNewExamen : examenListNew) {
                if (!examenListOld.contains(examenListNewExamen)) {
                    Alumno oldIdAlumnoOfExamenListNewExamen = examenListNewExamen.getIdAlumno();
                    examenListNewExamen.setIdAlumno(alumno);
                    examenListNewExamen = em.merge(examenListNewExamen);
                    if (oldIdAlumnoOfExamenListNewExamen != null && !oldIdAlumnoOfExamenListNewExamen.equals(alumno)) {
                        oldIdAlumnoOfExamenListNewExamen.getExamenList().remove(examenListNewExamen);
                        oldIdAlumnoOfExamenListNewExamen = em.merge(oldIdAlumnoOfExamenListNewExamen);
                    }
                }
            }
            for (Pagos pagosListNewPagos : pagosListNew) {
                if (!pagosListOld.contains(pagosListNewPagos)) {
                    Alumno oldIdAlumnoOfPagosListNewPagos = pagosListNewPagos.getIdAlumno();
                    pagosListNewPagos.setIdAlumno(alumno);
                    pagosListNewPagos = em.merge(pagosListNewPagos);
                    if (oldIdAlumnoOfPagosListNewPagos != null && !oldIdAlumnoOfPagosListNewPagos.equals(alumno)) {
                        oldIdAlumnoOfPagosListNewPagos.getPagosList().remove(pagosListNewPagos);
                        oldIdAlumnoOfPagosListNewPagos = em.merge(oldIdAlumnoOfPagosListNewPagos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = alumno.getIdAlumno();
                if (findAlumno(id) == null) {
                    throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno alumno;
            try {
                alumno = em.getReference(Alumno.class, id);
                alumno.getIdAlumno();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Tarea> tareaListOrphanCheck = alumno.getTareaList();
            for (Tarea tareaListOrphanCheckTarea : tareaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alumno (" + alumno + ") cannot be destroyed since the Tarea " + tareaListOrphanCheckTarea + " in its tareaList field has a non-nullable idAlumno field.");
            }
            List<Nota> notaListOrphanCheck = alumno.getNotaList();
            for (Nota notaListOrphanCheckNota : notaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alumno (" + alumno + ") cannot be destroyed since the Nota " + notaListOrphanCheckNota + " in its notaList field has a non-nullable idAlumno field.");
            }
            List<Examen> examenListOrphanCheck = alumno.getExamenList();
            for (Examen examenListOrphanCheckExamen : examenListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alumno (" + alumno + ") cannot be destroyed since the Examen " + examenListOrphanCheckExamen + " in its examenList field has a non-nullable idAlumno field.");
            }
            List<Pagos> pagosListOrphanCheck = alumno.getPagosList();
            for (Pagos pagosListOrphanCheckPagos : pagosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alumno (" + alumno + ") cannot be destroyed since the Pagos " + pagosListOrphanCheckPagos + " in its pagosList field has a non-nullable idAlumno field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Persona idPersona = alumno.getIdPersona();
            if (idPersona != null) {
                idPersona.getAlumnoList().remove(alumno);
                idPersona = em.merge(idPersona);
            }
            Carrera idCarrera = alumno.getIdCarrera();
            if (idCarrera != null) {
                idCarrera.getAlumnoList().remove(alumno);
                idCarrera = em.merge(idCarrera);
            }
            em.remove(alumno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Alumno> findAlumnoEntities() {
        return findAlumnoEntities(true, -1, -1);
    }

    public List<Alumno> findAlumnoEntities(int maxResults, int firstResult) {
        return findAlumnoEntities(false, maxResults, firstResult);
    }

    private List<Alumno> findAlumnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alumno.class));
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

    public Alumno findAlumno(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Alumno.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlumnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alumno> rt = cq.from(Alumno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
