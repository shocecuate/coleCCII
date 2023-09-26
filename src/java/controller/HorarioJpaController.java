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
import modelo.Curso;
import modelo.Asignacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Horario;
import modelo.MensajeAsistencia;

/**
 *
 * @author luismorales
 */
public class HorarioJpaController implements Serializable {

    public HorarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Horario horario) {
        if (horario.getAsignacionList() == null) {
            horario.setAsignacionList(new ArrayList<Asignacion>());
        }
        if (horario.getMensajeAsistenciaList() == null) {
            horario.setMensajeAsistenciaList(new ArrayList<MensajeAsistencia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Curso idCurso = horario.getIdCurso();
            if (idCurso != null) {
                idCurso = em.getReference(idCurso.getClass(), idCurso.getIdCurso());
                horario.setIdCurso(idCurso);
            }
            List<Asignacion> attachedAsignacionList = new ArrayList<Asignacion>();
            for (Asignacion asignacionListAsignacionToAttach : horario.getAsignacionList()) {
                asignacionListAsignacionToAttach = em.getReference(asignacionListAsignacionToAttach.getClass(), asignacionListAsignacionToAttach.getIdAsignacion());
                attachedAsignacionList.add(asignacionListAsignacionToAttach);
            }
            horario.setAsignacionList(attachedAsignacionList);
            List<MensajeAsistencia> attachedMensajeAsistenciaList = new ArrayList<MensajeAsistencia>();
            for (MensajeAsistencia mensajeAsistenciaListMensajeAsistenciaToAttach : horario.getMensajeAsistenciaList()) {
                mensajeAsistenciaListMensajeAsistenciaToAttach = em.getReference(mensajeAsistenciaListMensajeAsistenciaToAttach.getClass(), mensajeAsistenciaListMensajeAsistenciaToAttach.getMensajeAsistenciaPK());
                attachedMensajeAsistenciaList.add(mensajeAsistenciaListMensajeAsistenciaToAttach);
            }
            horario.setMensajeAsistenciaList(attachedMensajeAsistenciaList);
            em.persist(horario);
            if (idCurso != null) {
                idCurso.getHorarioList().add(horario);
                idCurso = em.merge(idCurso);
            }
            for (Asignacion asignacionListAsignacion : horario.getAsignacionList()) {
                Horario oldIdHorarioOfAsignacionListAsignacion = asignacionListAsignacion.getIdHorario();
                asignacionListAsignacion.setIdHorario(horario);
                asignacionListAsignacion = em.merge(asignacionListAsignacion);
                if (oldIdHorarioOfAsignacionListAsignacion != null) {
                    oldIdHorarioOfAsignacionListAsignacion.getAsignacionList().remove(asignacionListAsignacion);
                    oldIdHorarioOfAsignacionListAsignacion = em.merge(oldIdHorarioOfAsignacionListAsignacion);
                }
            }
            for (MensajeAsistencia mensajeAsistenciaListMensajeAsistencia : horario.getMensajeAsistenciaList()) {
                Horario oldHorarioOfMensajeAsistenciaListMensajeAsistencia = mensajeAsistenciaListMensajeAsistencia.getHorario();
                mensajeAsistenciaListMensajeAsistencia.setHorario(horario);
                mensajeAsistenciaListMensajeAsistencia = em.merge(mensajeAsistenciaListMensajeAsistencia);
                if (oldHorarioOfMensajeAsistenciaListMensajeAsistencia != null) {
                    oldHorarioOfMensajeAsistenciaListMensajeAsistencia.getMensajeAsistenciaList().remove(mensajeAsistenciaListMensajeAsistencia);
                    oldHorarioOfMensajeAsistenciaListMensajeAsistencia = em.merge(oldHorarioOfMensajeAsistenciaListMensajeAsistencia);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Horario horario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Horario persistentHorario = em.find(Horario.class, horario.getIdHorario());
            Curso idCursoOld = persistentHorario.getIdCurso();
            Curso idCursoNew = horario.getIdCurso();
            List<Asignacion> asignacionListOld = persistentHorario.getAsignacionList();
            List<Asignacion> asignacionListNew = horario.getAsignacionList();
            List<MensajeAsistencia> mensajeAsistenciaListOld = persistentHorario.getMensajeAsistenciaList();
            List<MensajeAsistencia> mensajeAsistenciaListNew = horario.getMensajeAsistenciaList();
            List<String> illegalOrphanMessages = null;
            for (Asignacion asignacionListOldAsignacion : asignacionListOld) {
                if (!asignacionListNew.contains(asignacionListOldAsignacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Asignacion " + asignacionListOldAsignacion + " since its idHorario field is not nullable.");
                }
            }
            for (MensajeAsistencia mensajeAsistenciaListOldMensajeAsistencia : mensajeAsistenciaListOld) {
                if (!mensajeAsistenciaListNew.contains(mensajeAsistenciaListOldMensajeAsistencia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MensajeAsistencia " + mensajeAsistenciaListOldMensajeAsistencia + " since its horario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCursoNew != null) {
                idCursoNew = em.getReference(idCursoNew.getClass(), idCursoNew.getIdCurso());
                horario.setIdCurso(idCursoNew);
            }
            List<Asignacion> attachedAsignacionListNew = new ArrayList<Asignacion>();
            for (Asignacion asignacionListNewAsignacionToAttach : asignacionListNew) {
                asignacionListNewAsignacionToAttach = em.getReference(asignacionListNewAsignacionToAttach.getClass(), asignacionListNewAsignacionToAttach.getIdAsignacion());
                attachedAsignacionListNew.add(asignacionListNewAsignacionToAttach);
            }
            asignacionListNew = attachedAsignacionListNew;
            horario.setAsignacionList(asignacionListNew);
            List<MensajeAsistencia> attachedMensajeAsistenciaListNew = new ArrayList<MensajeAsistencia>();
            for (MensajeAsistencia mensajeAsistenciaListNewMensajeAsistenciaToAttach : mensajeAsistenciaListNew) {
                mensajeAsistenciaListNewMensajeAsistenciaToAttach = em.getReference(mensajeAsistenciaListNewMensajeAsistenciaToAttach.getClass(), mensajeAsistenciaListNewMensajeAsistenciaToAttach.getMensajeAsistenciaPK());
                attachedMensajeAsistenciaListNew.add(mensajeAsistenciaListNewMensajeAsistenciaToAttach);
            }
            mensajeAsistenciaListNew = attachedMensajeAsistenciaListNew;
            horario.setMensajeAsistenciaList(mensajeAsistenciaListNew);
            horario = em.merge(horario);
            if (idCursoOld != null && !idCursoOld.equals(idCursoNew)) {
                idCursoOld.getHorarioList().remove(horario);
                idCursoOld = em.merge(idCursoOld);
            }
            if (idCursoNew != null && !idCursoNew.equals(idCursoOld)) {
                idCursoNew.getHorarioList().add(horario);
                idCursoNew = em.merge(idCursoNew);
            }
            for (Asignacion asignacionListNewAsignacion : asignacionListNew) {
                if (!asignacionListOld.contains(asignacionListNewAsignacion)) {
                    Horario oldIdHorarioOfAsignacionListNewAsignacion = asignacionListNewAsignacion.getIdHorario();
                    asignacionListNewAsignacion.setIdHorario(horario);
                    asignacionListNewAsignacion = em.merge(asignacionListNewAsignacion);
                    if (oldIdHorarioOfAsignacionListNewAsignacion != null && !oldIdHorarioOfAsignacionListNewAsignacion.equals(horario)) {
                        oldIdHorarioOfAsignacionListNewAsignacion.getAsignacionList().remove(asignacionListNewAsignacion);
                        oldIdHorarioOfAsignacionListNewAsignacion = em.merge(oldIdHorarioOfAsignacionListNewAsignacion);
                    }
                }
            }
            for (MensajeAsistencia mensajeAsistenciaListNewMensajeAsistencia : mensajeAsistenciaListNew) {
                if (!mensajeAsistenciaListOld.contains(mensajeAsistenciaListNewMensajeAsistencia)) {
                    Horario oldHorarioOfMensajeAsistenciaListNewMensajeAsistencia = mensajeAsistenciaListNewMensajeAsistencia.getHorario();
                    mensajeAsistenciaListNewMensajeAsistencia.setHorario(horario);
                    mensajeAsistenciaListNewMensajeAsistencia = em.merge(mensajeAsistenciaListNewMensajeAsistencia);
                    if (oldHorarioOfMensajeAsistenciaListNewMensajeAsistencia != null && !oldHorarioOfMensajeAsistenciaListNewMensajeAsistencia.equals(horario)) {
                        oldHorarioOfMensajeAsistenciaListNewMensajeAsistencia.getMensajeAsistenciaList().remove(mensajeAsistenciaListNewMensajeAsistencia);
                        oldHorarioOfMensajeAsistenciaListNewMensajeAsistencia = em.merge(oldHorarioOfMensajeAsistenciaListNewMensajeAsistencia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = horario.getIdHorario();
                if (findHorario(id) == null) {
                    throw new NonexistentEntityException("The horario with id " + id + " no longer exists.");
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
            Horario horario;
            try {
                horario = em.getReference(Horario.class, id);
                horario.getIdHorario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The horario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Asignacion> asignacionListOrphanCheck = horario.getAsignacionList();
            for (Asignacion asignacionListOrphanCheckAsignacion : asignacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Horario (" + horario + ") cannot be destroyed since the Asignacion " + asignacionListOrphanCheckAsignacion + " in its asignacionList field has a non-nullable idHorario field.");
            }
            List<MensajeAsistencia> mensajeAsistenciaListOrphanCheck = horario.getMensajeAsistenciaList();
            for (MensajeAsistencia mensajeAsistenciaListOrphanCheckMensajeAsistencia : mensajeAsistenciaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Horario (" + horario + ") cannot be destroyed since the MensajeAsistencia " + mensajeAsistenciaListOrphanCheckMensajeAsistencia + " in its mensajeAsistenciaList field has a non-nullable horario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Curso idCurso = horario.getIdCurso();
            if (idCurso != null) {
                idCurso.getHorarioList().remove(horario);
                idCurso = em.merge(idCurso);
            }
            em.remove(horario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Horario> findHorarioEntities() {
        return findHorarioEntities(true, -1, -1);
    }

    public List<Horario> findHorarioEntities(int maxResults, int firstResult) {
        return findHorarioEntities(false, maxResults, firstResult);
    }

    private List<Horario> findHorarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Horario.class));
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

    public Horario findHorario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Horario.class, id);
        } finally {
            em.close();
        }
    }

    public int getHorarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Horario> rt = cq.from(Horario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
