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
import modelo.Tipodepago;
import modelo.Cursoporcarrera;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Pagos;

/**
 *
 * @author luismorales
 */
public class PagosJpaController implements Serializable {

    public PagosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pagos pagos) {
        if (pagos.getCursoporcarreraList() == null) {
            pagos.setCursoporcarreraList(new ArrayList<Cursoporcarrera>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno idAlumno = pagos.getIdAlumno();
            if (idAlumno != null) {
                idAlumno = em.getReference(idAlumno.getClass(), idAlumno.getIdAlumno());
                pagos.setIdAlumno(idAlumno);
            }
            Tipodepago idTipoPago = pagos.getIdTipoPago();
            if (idTipoPago != null) {
                idTipoPago = em.getReference(idTipoPago.getClass(), idTipoPago.getIdTipoPago());
                pagos.setIdTipoPago(idTipoPago);
            }
            List<Cursoporcarrera> attachedCursoporcarreraList = new ArrayList<Cursoporcarrera>();
            for (Cursoporcarrera cursoporcarreraListCursoporcarreraToAttach : pagos.getCursoporcarreraList()) {
                cursoporcarreraListCursoporcarreraToAttach = em.getReference(cursoporcarreraListCursoporcarreraToAttach.getClass(), cursoporcarreraListCursoporcarreraToAttach.getIdCursoPorCarrera());
                attachedCursoporcarreraList.add(cursoporcarreraListCursoporcarreraToAttach);
            }
            pagos.setCursoporcarreraList(attachedCursoporcarreraList);
            em.persist(pagos);
            if (idAlumno != null) {
                idAlumno.getPagosList().add(pagos);
                idAlumno = em.merge(idAlumno);
            }
            if (idTipoPago != null) {
                idTipoPago.getPagosList().add(pagos);
                idTipoPago = em.merge(idTipoPago);
            }
            for (Cursoporcarrera cursoporcarreraListCursoporcarrera : pagos.getCursoporcarreraList()) {
                cursoporcarreraListCursoporcarrera.getPagosList().add(pagos);
                cursoporcarreraListCursoporcarrera = em.merge(cursoporcarreraListCursoporcarrera);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pagos pagos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pagos persistentPagos = em.find(Pagos.class, pagos.getIdPago());
            Alumno idAlumnoOld = persistentPagos.getIdAlumno();
            Alumno idAlumnoNew = pagos.getIdAlumno();
            Tipodepago idTipoPagoOld = persistentPagos.getIdTipoPago();
            Tipodepago idTipoPagoNew = pagos.getIdTipoPago();
            List<Cursoporcarrera> cursoporcarreraListOld = persistentPagos.getCursoporcarreraList();
            List<Cursoporcarrera> cursoporcarreraListNew = pagos.getCursoporcarreraList();
            if (idAlumnoNew != null) {
                idAlumnoNew = em.getReference(idAlumnoNew.getClass(), idAlumnoNew.getIdAlumno());
                pagos.setIdAlumno(idAlumnoNew);
            }
            if (idTipoPagoNew != null) {
                idTipoPagoNew = em.getReference(idTipoPagoNew.getClass(), idTipoPagoNew.getIdTipoPago());
                pagos.setIdTipoPago(idTipoPagoNew);
            }
            List<Cursoporcarrera> attachedCursoporcarreraListNew = new ArrayList<Cursoporcarrera>();
            for (Cursoporcarrera cursoporcarreraListNewCursoporcarreraToAttach : cursoporcarreraListNew) {
                cursoporcarreraListNewCursoporcarreraToAttach = em.getReference(cursoporcarreraListNewCursoporcarreraToAttach.getClass(), cursoporcarreraListNewCursoporcarreraToAttach.getIdCursoPorCarrera());
                attachedCursoporcarreraListNew.add(cursoporcarreraListNewCursoporcarreraToAttach);
            }
            cursoporcarreraListNew = attachedCursoporcarreraListNew;
            pagos.setCursoporcarreraList(cursoporcarreraListNew);
            pagos = em.merge(pagos);
            if (idAlumnoOld != null && !idAlumnoOld.equals(idAlumnoNew)) {
                idAlumnoOld.getPagosList().remove(pagos);
                idAlumnoOld = em.merge(idAlumnoOld);
            }
            if (idAlumnoNew != null && !idAlumnoNew.equals(idAlumnoOld)) {
                idAlumnoNew.getPagosList().add(pagos);
                idAlumnoNew = em.merge(idAlumnoNew);
            }
            if (idTipoPagoOld != null && !idTipoPagoOld.equals(idTipoPagoNew)) {
                idTipoPagoOld.getPagosList().remove(pagos);
                idTipoPagoOld = em.merge(idTipoPagoOld);
            }
            if (idTipoPagoNew != null && !idTipoPagoNew.equals(idTipoPagoOld)) {
                idTipoPagoNew.getPagosList().add(pagos);
                idTipoPagoNew = em.merge(idTipoPagoNew);
            }
            for (Cursoporcarrera cursoporcarreraListOldCursoporcarrera : cursoporcarreraListOld) {
                if (!cursoporcarreraListNew.contains(cursoporcarreraListOldCursoporcarrera)) {
                    cursoporcarreraListOldCursoporcarrera.getPagosList().remove(pagos);
                    cursoporcarreraListOldCursoporcarrera = em.merge(cursoporcarreraListOldCursoporcarrera);
                }
            }
            for (Cursoporcarrera cursoporcarreraListNewCursoporcarrera : cursoporcarreraListNew) {
                if (!cursoporcarreraListOld.contains(cursoporcarreraListNewCursoporcarrera)) {
                    cursoporcarreraListNewCursoporcarrera.getPagosList().add(pagos);
                    cursoporcarreraListNewCursoporcarrera = em.merge(cursoporcarreraListNewCursoporcarrera);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pagos.getIdPago();
                if (findPagos(id) == null) {
                    throw new NonexistentEntityException("The pagos with id " + id + " no longer exists.");
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
            Pagos pagos;
            try {
                pagos = em.getReference(Pagos.class, id);
                pagos.getIdPago();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pagos with id " + id + " no longer exists.", enfe);
            }
            Alumno idAlumno = pagos.getIdAlumno();
            if (idAlumno != null) {
                idAlumno.getPagosList().remove(pagos);
                idAlumno = em.merge(idAlumno);
            }
            Tipodepago idTipoPago = pagos.getIdTipoPago();
            if (idTipoPago != null) {
                idTipoPago.getPagosList().remove(pagos);
                idTipoPago = em.merge(idTipoPago);
            }
            List<Cursoporcarrera> cursoporcarreraList = pagos.getCursoporcarreraList();
            for (Cursoporcarrera cursoporcarreraListCursoporcarrera : cursoporcarreraList) {
                cursoporcarreraListCursoporcarrera.getPagosList().remove(pagos);
                cursoporcarreraListCursoporcarrera = em.merge(cursoporcarreraListCursoporcarrera);
            }
            em.remove(pagos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pagos> findPagosEntities() {
        return findPagosEntities(true, -1, -1);
    }

    public List<Pagos> findPagosEntities(int maxResults, int firstResult) {
        return findPagosEntities(false, maxResults, firstResult);
    }

    private List<Pagos> findPagosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pagos.class));
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

    public Pagos findPagos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pagos.class, id);
        } finally {
            em.close();
        }
    }

    public int getPagosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pagos> rt = cq.from(Pagos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
