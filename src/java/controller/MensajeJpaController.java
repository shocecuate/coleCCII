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
import modelo.Tarea;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Mensaje;
import modelo.MensajeAsistencia;

/**
 *
 * @author luismorales
 */
public class MensajeJpaController implements Serializable {

    public MensajeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Mensaje mensaje) {
        if (mensaje.getTareaList() == null) {
            mensaje.setTareaList(new ArrayList<Tarea>());
        }
        if (mensaje.getTareaList1() == null) {
            mensaje.setTareaList1(new ArrayList<Tarea>());
        }
        if (mensaje.getMensajeAsistenciaList() == null) {
            mensaje.setMensajeAsistenciaList(new ArrayList<MensajeAsistencia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona idDestinatario = mensaje.getIdDestinatario();
            if (idDestinatario != null) {
                idDestinatario = em.getReference(idDestinatario.getClass(), idDestinatario.getIdPersona());
                mensaje.setIdDestinatario(idDestinatario);
            }
            List<Tarea> attachedTareaList = new ArrayList<Tarea>();
            for (Tarea tareaListTareaToAttach : mensaje.getTareaList()) {
                tareaListTareaToAttach = em.getReference(tareaListTareaToAttach.getClass(), tareaListTareaToAttach.getIdTarea());
                attachedTareaList.add(tareaListTareaToAttach);
            }
            mensaje.setTareaList(attachedTareaList);
            List<Tarea> attachedTareaList1 = new ArrayList<Tarea>();
            for (Tarea tareaList1TareaToAttach : mensaje.getTareaList1()) {
                tareaList1TareaToAttach = em.getReference(tareaList1TareaToAttach.getClass(), tareaList1TareaToAttach.getIdTarea());
                attachedTareaList1.add(tareaList1TareaToAttach);
            }
            mensaje.setTareaList1(attachedTareaList1);
            List<MensajeAsistencia> attachedMensajeAsistenciaList = new ArrayList<MensajeAsistencia>();
            for (MensajeAsistencia mensajeAsistenciaListMensajeAsistenciaToAttach : mensaje.getMensajeAsistenciaList()) {
                mensajeAsistenciaListMensajeAsistenciaToAttach = em.getReference(mensajeAsistenciaListMensajeAsistenciaToAttach.getClass(), mensajeAsistenciaListMensajeAsistenciaToAttach.getMensajeAsistenciaPK());
                attachedMensajeAsistenciaList.add(mensajeAsistenciaListMensajeAsistenciaToAttach);
            }
            mensaje.setMensajeAsistenciaList(attachedMensajeAsistenciaList);
            em.persist(mensaje);
            if (idDestinatario != null) {
                idDestinatario.getMensajeList().add(mensaje);
                idDestinatario = em.merge(idDestinatario);
            }
            for (Tarea tareaListTarea : mensaje.getTareaList()) {
                tareaListTarea.getMensajeList().add(mensaje);
                tareaListTarea = em.merge(tareaListTarea);
            }
            for (Tarea tareaList1Tarea : mensaje.getTareaList1()) {
                tareaList1Tarea.getMensajeList().add(mensaje);
                tareaList1Tarea = em.merge(tareaList1Tarea);
            }
            for (MensajeAsistencia mensajeAsistenciaListMensajeAsistencia : mensaje.getMensajeAsistenciaList()) {
                Mensaje oldMensajeOfMensajeAsistenciaListMensajeAsistencia = mensajeAsistenciaListMensajeAsistencia.getMensaje();
                mensajeAsistenciaListMensajeAsistencia.setMensaje(mensaje);
                mensajeAsistenciaListMensajeAsistencia = em.merge(mensajeAsistenciaListMensajeAsistencia);
                if (oldMensajeOfMensajeAsistenciaListMensajeAsistencia != null) {
                    oldMensajeOfMensajeAsistenciaListMensajeAsistencia.getMensajeAsistenciaList().remove(mensajeAsistenciaListMensajeAsistencia);
                    oldMensajeOfMensajeAsistenciaListMensajeAsistencia = em.merge(oldMensajeOfMensajeAsistenciaListMensajeAsistencia);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Mensaje mensaje) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mensaje persistentMensaje = em.find(Mensaje.class, mensaje.getIdMensaje());
            Persona idDestinatarioOld = persistentMensaje.getIdDestinatario();
            Persona idDestinatarioNew = mensaje.getIdDestinatario();
            List<Tarea> tareaListOld = persistentMensaje.getTareaList();
            List<Tarea> tareaListNew = mensaje.getTareaList();
            List<Tarea> tareaList1Old = persistentMensaje.getTareaList1();
            List<Tarea> tareaList1New = mensaje.getTareaList1();
            List<MensajeAsistencia> mensajeAsistenciaListOld = persistentMensaje.getMensajeAsistenciaList();
            List<MensajeAsistencia> mensajeAsistenciaListNew = mensaje.getMensajeAsistenciaList();
            List<String> illegalOrphanMessages = null;
            for (MensajeAsistencia mensajeAsistenciaListOldMensajeAsistencia : mensajeAsistenciaListOld) {
                if (!mensajeAsistenciaListNew.contains(mensajeAsistenciaListOldMensajeAsistencia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MensajeAsistencia " + mensajeAsistenciaListOldMensajeAsistencia + " since its mensaje field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idDestinatarioNew != null) {
                idDestinatarioNew = em.getReference(idDestinatarioNew.getClass(), idDestinatarioNew.getIdPersona());
                mensaje.setIdDestinatario(idDestinatarioNew);
            }
            List<Tarea> attachedTareaListNew = new ArrayList<Tarea>();
            for (Tarea tareaListNewTareaToAttach : tareaListNew) {
                tareaListNewTareaToAttach = em.getReference(tareaListNewTareaToAttach.getClass(), tareaListNewTareaToAttach.getIdTarea());
                attachedTareaListNew.add(tareaListNewTareaToAttach);
            }
            tareaListNew = attachedTareaListNew;
            mensaje.setTareaList(tareaListNew);
            List<Tarea> attachedTareaList1New = new ArrayList<Tarea>();
            for (Tarea tareaList1NewTareaToAttach : tareaList1New) {
                tareaList1NewTareaToAttach = em.getReference(tareaList1NewTareaToAttach.getClass(), tareaList1NewTareaToAttach.getIdTarea());
                attachedTareaList1New.add(tareaList1NewTareaToAttach);
            }
            tareaList1New = attachedTareaList1New;
            mensaje.setTareaList1(tareaList1New);
            List<MensajeAsistencia> attachedMensajeAsistenciaListNew = new ArrayList<MensajeAsistencia>();
            for (MensajeAsistencia mensajeAsistenciaListNewMensajeAsistenciaToAttach : mensajeAsistenciaListNew) {
                mensajeAsistenciaListNewMensajeAsistenciaToAttach = em.getReference(mensajeAsistenciaListNewMensajeAsistenciaToAttach.getClass(), mensajeAsistenciaListNewMensajeAsistenciaToAttach.getMensajeAsistenciaPK());
                attachedMensajeAsistenciaListNew.add(mensajeAsistenciaListNewMensajeAsistenciaToAttach);
            }
            mensajeAsistenciaListNew = attachedMensajeAsistenciaListNew;
            mensaje.setMensajeAsistenciaList(mensajeAsistenciaListNew);
            mensaje = em.merge(mensaje);
            if (idDestinatarioOld != null && !idDestinatarioOld.equals(idDestinatarioNew)) {
                idDestinatarioOld.getMensajeList().remove(mensaje);
                idDestinatarioOld = em.merge(idDestinatarioOld);
            }
            if (idDestinatarioNew != null && !idDestinatarioNew.equals(idDestinatarioOld)) {
                idDestinatarioNew.getMensajeList().add(mensaje);
                idDestinatarioNew = em.merge(idDestinatarioNew);
            }
            for (Tarea tareaListOldTarea : tareaListOld) {
                if (!tareaListNew.contains(tareaListOldTarea)) {
                    tareaListOldTarea.getMensajeList().remove(mensaje);
                    tareaListOldTarea = em.merge(tareaListOldTarea);
                }
            }
            for (Tarea tareaListNewTarea : tareaListNew) {
                if (!tareaListOld.contains(tareaListNewTarea)) {
                    tareaListNewTarea.getMensajeList().add(mensaje);
                    tareaListNewTarea = em.merge(tareaListNewTarea);
                }
            }
            for (Tarea tareaList1OldTarea : tareaList1Old) {
                if (!tareaList1New.contains(tareaList1OldTarea)) {
                    tareaList1OldTarea.getMensajeList().remove(mensaje);
                    tareaList1OldTarea = em.merge(tareaList1OldTarea);
                }
            }
            for (Tarea tareaList1NewTarea : tareaList1New) {
                if (!tareaList1Old.contains(tareaList1NewTarea)) {
                    tareaList1NewTarea.getMensajeList().add(mensaje);
                    tareaList1NewTarea = em.merge(tareaList1NewTarea);
                }
            }
            for (MensajeAsistencia mensajeAsistenciaListNewMensajeAsistencia : mensajeAsistenciaListNew) {
                if (!mensajeAsistenciaListOld.contains(mensajeAsistenciaListNewMensajeAsistencia)) {
                    Mensaje oldMensajeOfMensajeAsistenciaListNewMensajeAsistencia = mensajeAsistenciaListNewMensajeAsistencia.getMensaje();
                    mensajeAsistenciaListNewMensajeAsistencia.setMensaje(mensaje);
                    mensajeAsistenciaListNewMensajeAsistencia = em.merge(mensajeAsistenciaListNewMensajeAsistencia);
                    if (oldMensajeOfMensajeAsistenciaListNewMensajeAsistencia != null && !oldMensajeOfMensajeAsistenciaListNewMensajeAsistencia.equals(mensaje)) {
                        oldMensajeOfMensajeAsistenciaListNewMensajeAsistencia.getMensajeAsistenciaList().remove(mensajeAsistenciaListNewMensajeAsistencia);
                        oldMensajeOfMensajeAsistenciaListNewMensajeAsistencia = em.merge(oldMensajeOfMensajeAsistenciaListNewMensajeAsistencia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = mensaje.getIdMensaje();
                if (findMensaje(id) == null) {
                    throw new NonexistentEntityException("The mensaje with id " + id + " no longer exists.");
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
            Mensaje mensaje;
            try {
                mensaje = em.getReference(Mensaje.class, id);
                mensaje.getIdMensaje();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mensaje with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<MensajeAsistencia> mensajeAsistenciaListOrphanCheck = mensaje.getMensajeAsistenciaList();
            for (MensajeAsistencia mensajeAsistenciaListOrphanCheckMensajeAsistencia : mensajeAsistenciaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Mensaje (" + mensaje + ") cannot be destroyed since the MensajeAsistencia " + mensajeAsistenciaListOrphanCheckMensajeAsistencia + " in its mensajeAsistenciaList field has a non-nullable mensaje field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Persona idDestinatario = mensaje.getIdDestinatario();
            if (idDestinatario != null) {
                idDestinatario.getMensajeList().remove(mensaje);
                idDestinatario = em.merge(idDestinatario);
            }
            List<Tarea> tareaList = mensaje.getTareaList();
            for (Tarea tareaListTarea : tareaList) {
                tareaListTarea.getMensajeList().remove(mensaje);
                tareaListTarea = em.merge(tareaListTarea);
            }
            List<Tarea> tareaList1 = mensaje.getTareaList1();
            for (Tarea tareaList1Tarea : tareaList1) {
                tareaList1Tarea.getMensajeList().remove(mensaje);
                tareaList1Tarea = em.merge(tareaList1Tarea);
            }
            em.remove(mensaje);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Mensaje> findMensajeEntities() {
        return findMensajeEntities(true, -1, -1);
    }

    public List<Mensaje> findMensajeEntities(int maxResults, int firstResult) {
        return findMensajeEntities(false, maxResults, firstResult);
    }

    private List<Mensaje> findMensajeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Mensaje.class));
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

    public Mensaje findMensaje(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Mensaje.class, id);
        } finally {
            em.close();
        }
    }

    public int getMensajeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Mensaje> rt = cq.from(Mensaje.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
