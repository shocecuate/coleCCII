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
import modelo.Materia;
import modelo.Carrera;
import modelo.Curso;
import modelo.Pagos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Cursoporcarrera;
import modelo.Inscripcion;

/**
 *
 * @author luismorales
 */
public class CursoporcarreraJpaController implements Serializable {

    public CursoporcarreraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cursoporcarrera cursoporcarrera) {
        if (cursoporcarrera.getPagosList() == null) {
            cursoporcarrera.setPagosList(new ArrayList<Pagos>());
        }
        if (cursoporcarrera.getInscripcionList() == null) {
            cursoporcarrera.setInscripcionList(new ArrayList<Inscripcion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia idMateria = cursoporcarrera.getIdMateria();
            if (idMateria != null) {
                idMateria = em.getReference(idMateria.getClass(), idMateria.getIdMateria());
                cursoporcarrera.setIdMateria(idMateria);
            }
            Carrera idCarrera = cursoporcarrera.getIdCarrera();
            if (idCarrera != null) {
                idCarrera = em.getReference(idCarrera.getClass(), idCarrera.getIdCarrera());
                cursoporcarrera.setIdCarrera(idCarrera);
            }
            Curso idCurso = cursoporcarrera.getIdCurso();
            if (idCurso != null) {
                idCurso = em.getReference(idCurso.getClass(), idCurso.getIdCurso());
                cursoporcarrera.setIdCurso(idCurso);
            }
            List<Pagos> attachedPagosList = new ArrayList<Pagos>();
            for (Pagos pagosListPagosToAttach : cursoporcarrera.getPagosList()) {
                pagosListPagosToAttach = em.getReference(pagosListPagosToAttach.getClass(), pagosListPagosToAttach.getIdPago());
                attachedPagosList.add(pagosListPagosToAttach);
            }
            cursoporcarrera.setPagosList(attachedPagosList);
            List<Inscripcion> attachedInscripcionList = new ArrayList<Inscripcion>();
            for (Inscripcion inscripcionListInscripcionToAttach : cursoporcarrera.getInscripcionList()) {
                inscripcionListInscripcionToAttach = em.getReference(inscripcionListInscripcionToAttach.getClass(), inscripcionListInscripcionToAttach.getIdInscripcion());
                attachedInscripcionList.add(inscripcionListInscripcionToAttach);
            }
            cursoporcarrera.setInscripcionList(attachedInscripcionList);
            em.persist(cursoporcarrera);
            if (idMateria != null) {
                idMateria.getCursoporcarreraList().add(cursoporcarrera);
                idMateria = em.merge(idMateria);
            }
            if (idCarrera != null) {
                idCarrera.getCursoporcarreraList().add(cursoporcarrera);
                idCarrera = em.merge(idCarrera);
            }
            if (idCurso != null) {
                idCurso.getCursoporcarreraList().add(cursoporcarrera);
                idCurso = em.merge(idCurso);
            }
            for (Pagos pagosListPagos : cursoporcarrera.getPagosList()) {
                pagosListPagos.getCursoporcarreraList().add(cursoporcarrera);
                pagosListPagos = em.merge(pagosListPagos);
            }
            for (Inscripcion inscripcionListInscripcion : cursoporcarrera.getInscripcionList()) {
                Cursoporcarrera oldIdCursoporCarreraOfInscripcionListInscripcion = inscripcionListInscripcion.getIdCursoporCarrera();
                inscripcionListInscripcion.setIdCursoporCarrera(cursoporcarrera);
                inscripcionListInscripcion = em.merge(inscripcionListInscripcion);
                if (oldIdCursoporCarreraOfInscripcionListInscripcion != null) {
                    oldIdCursoporCarreraOfInscripcionListInscripcion.getInscripcionList().remove(inscripcionListInscripcion);
                    oldIdCursoporCarreraOfInscripcionListInscripcion = em.merge(oldIdCursoporCarreraOfInscripcionListInscripcion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cursoporcarrera cursoporcarrera) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cursoporcarrera persistentCursoporcarrera = em.find(Cursoporcarrera.class, cursoporcarrera.getIdCursoPorCarrera());
            Materia idMateriaOld = persistentCursoporcarrera.getIdMateria();
            Materia idMateriaNew = cursoporcarrera.getIdMateria();
            Carrera idCarreraOld = persistentCursoporcarrera.getIdCarrera();
            Carrera idCarreraNew = cursoporcarrera.getIdCarrera();
            Curso idCursoOld = persistentCursoporcarrera.getIdCurso();
            Curso idCursoNew = cursoporcarrera.getIdCurso();
            List<Pagos> pagosListOld = persistentCursoporcarrera.getPagosList();
            List<Pagos> pagosListNew = cursoporcarrera.getPagosList();
            List<Inscripcion> inscripcionListOld = persistentCursoporcarrera.getInscripcionList();
            List<Inscripcion> inscripcionListNew = cursoporcarrera.getInscripcionList();
            List<String> illegalOrphanMessages = null;
            for (Inscripcion inscripcionListOldInscripcion : inscripcionListOld) {
                if (!inscripcionListNew.contains(inscripcionListOldInscripcion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Inscripcion " + inscripcionListOldInscripcion + " since its idCursoporCarrera field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idMateriaNew != null) {
                idMateriaNew = em.getReference(idMateriaNew.getClass(), idMateriaNew.getIdMateria());
                cursoporcarrera.setIdMateria(idMateriaNew);
            }
            if (idCarreraNew != null) {
                idCarreraNew = em.getReference(idCarreraNew.getClass(), idCarreraNew.getIdCarrera());
                cursoporcarrera.setIdCarrera(idCarreraNew);
            }
            if (idCursoNew != null) {
                idCursoNew = em.getReference(idCursoNew.getClass(), idCursoNew.getIdCurso());
                cursoporcarrera.setIdCurso(idCursoNew);
            }
            List<Pagos> attachedPagosListNew = new ArrayList<Pagos>();
            for (Pagos pagosListNewPagosToAttach : pagosListNew) {
                pagosListNewPagosToAttach = em.getReference(pagosListNewPagosToAttach.getClass(), pagosListNewPagosToAttach.getIdPago());
                attachedPagosListNew.add(pagosListNewPagosToAttach);
            }
            pagosListNew = attachedPagosListNew;
            cursoporcarrera.setPagosList(pagosListNew);
            List<Inscripcion> attachedInscripcionListNew = new ArrayList<Inscripcion>();
            for (Inscripcion inscripcionListNewInscripcionToAttach : inscripcionListNew) {
                inscripcionListNewInscripcionToAttach = em.getReference(inscripcionListNewInscripcionToAttach.getClass(), inscripcionListNewInscripcionToAttach.getIdInscripcion());
                attachedInscripcionListNew.add(inscripcionListNewInscripcionToAttach);
            }
            inscripcionListNew = attachedInscripcionListNew;
            cursoporcarrera.setInscripcionList(inscripcionListNew);
            cursoporcarrera = em.merge(cursoporcarrera);
            if (idMateriaOld != null && !idMateriaOld.equals(idMateriaNew)) {
                idMateriaOld.getCursoporcarreraList().remove(cursoporcarrera);
                idMateriaOld = em.merge(idMateriaOld);
            }
            if (idMateriaNew != null && !idMateriaNew.equals(idMateriaOld)) {
                idMateriaNew.getCursoporcarreraList().add(cursoporcarrera);
                idMateriaNew = em.merge(idMateriaNew);
            }
            if (idCarreraOld != null && !idCarreraOld.equals(idCarreraNew)) {
                idCarreraOld.getCursoporcarreraList().remove(cursoporcarrera);
                idCarreraOld = em.merge(idCarreraOld);
            }
            if (idCarreraNew != null && !idCarreraNew.equals(idCarreraOld)) {
                idCarreraNew.getCursoporcarreraList().add(cursoporcarrera);
                idCarreraNew = em.merge(idCarreraNew);
            }
            if (idCursoOld != null && !idCursoOld.equals(idCursoNew)) {
                idCursoOld.getCursoporcarreraList().remove(cursoporcarrera);
                idCursoOld = em.merge(idCursoOld);
            }
            if (idCursoNew != null && !idCursoNew.equals(idCursoOld)) {
                idCursoNew.getCursoporcarreraList().add(cursoporcarrera);
                idCursoNew = em.merge(idCursoNew);
            }
            for (Pagos pagosListOldPagos : pagosListOld) {
                if (!pagosListNew.contains(pagosListOldPagos)) {
                    pagosListOldPagos.getCursoporcarreraList().remove(cursoporcarrera);
                    pagosListOldPagos = em.merge(pagosListOldPagos);
                }
            }
            for (Pagos pagosListNewPagos : pagosListNew) {
                if (!pagosListOld.contains(pagosListNewPagos)) {
                    pagosListNewPagos.getCursoporcarreraList().add(cursoporcarrera);
                    pagosListNewPagos = em.merge(pagosListNewPagos);
                }
            }
            for (Inscripcion inscripcionListNewInscripcion : inscripcionListNew) {
                if (!inscripcionListOld.contains(inscripcionListNewInscripcion)) {
                    Cursoporcarrera oldIdCursoporCarreraOfInscripcionListNewInscripcion = inscripcionListNewInscripcion.getIdCursoporCarrera();
                    inscripcionListNewInscripcion.setIdCursoporCarrera(cursoporcarrera);
                    inscripcionListNewInscripcion = em.merge(inscripcionListNewInscripcion);
                    if (oldIdCursoporCarreraOfInscripcionListNewInscripcion != null && !oldIdCursoporCarreraOfInscripcionListNewInscripcion.equals(cursoporcarrera)) {
                        oldIdCursoporCarreraOfInscripcionListNewInscripcion.getInscripcionList().remove(inscripcionListNewInscripcion);
                        oldIdCursoporCarreraOfInscripcionListNewInscripcion = em.merge(oldIdCursoporCarreraOfInscripcionListNewInscripcion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cursoporcarrera.getIdCursoPorCarrera();
                if (findCursoporcarrera(id) == null) {
                    throw new NonexistentEntityException("The cursoporcarrera with id " + id + " no longer exists.");
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
            Cursoporcarrera cursoporcarrera;
            try {
                cursoporcarrera = em.getReference(Cursoporcarrera.class, id);
                cursoporcarrera.getIdCursoPorCarrera();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cursoporcarrera with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Inscripcion> inscripcionListOrphanCheck = cursoporcarrera.getInscripcionList();
            for (Inscripcion inscripcionListOrphanCheckInscripcion : inscripcionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cursoporcarrera (" + cursoporcarrera + ") cannot be destroyed since the Inscripcion " + inscripcionListOrphanCheckInscripcion + " in its inscripcionList field has a non-nullable idCursoporCarrera field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Materia idMateria = cursoporcarrera.getIdMateria();
            if (idMateria != null) {
                idMateria.getCursoporcarreraList().remove(cursoporcarrera);
                idMateria = em.merge(idMateria);
            }
            Carrera idCarrera = cursoporcarrera.getIdCarrera();
            if (idCarrera != null) {
                idCarrera.getCursoporcarreraList().remove(cursoporcarrera);
                idCarrera = em.merge(idCarrera);
            }
            Curso idCurso = cursoporcarrera.getIdCurso();
            if (idCurso != null) {
                idCurso.getCursoporcarreraList().remove(cursoporcarrera);
                idCurso = em.merge(idCurso);
            }
            List<Pagos> pagosList = cursoporcarrera.getPagosList();
            for (Pagos pagosListPagos : pagosList) {
                pagosListPagos.getCursoporcarreraList().remove(cursoporcarrera);
                pagosListPagos = em.merge(pagosListPagos);
            }
            em.remove(cursoporcarrera);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cursoporcarrera> findCursoporcarreraEntities() {
        return findCursoporcarreraEntities(true, -1, -1);
    }

    public List<Cursoporcarrera> findCursoporcarreraEntities(int maxResults, int firstResult) {
        return findCursoporcarreraEntities(false, maxResults, firstResult);
    }

    private List<Cursoporcarrera> findCursoporcarreraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cursoporcarrera.class));
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

    public Cursoporcarrera findCursoporcarrera(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cursoporcarrera.class, id);
        } finally {
            em.close();
        }
    }

    public int getCursoporcarreraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cursoporcarrera> rt = cq.from(Cursoporcarrera.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
