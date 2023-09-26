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
import modelo.Pagos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Inscripcion;
import modelo.Tipodepago;

/**
 *
 * @author luismorales
 */
public class TipodepagoJpaController implements Serializable {

    public TipodepagoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipodepago tipodepago) {
        if (tipodepago.getPagosList() == null) {
            tipodepago.setPagosList(new ArrayList<Pagos>());
        }
        if (tipodepago.getInscripcionList() == null) {
            tipodepago.setInscripcionList(new ArrayList<Inscripcion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Pagos> attachedPagosList = new ArrayList<Pagos>();
            for (Pagos pagosListPagosToAttach : tipodepago.getPagosList()) {
                pagosListPagosToAttach = em.getReference(pagosListPagosToAttach.getClass(), pagosListPagosToAttach.getIdPago());
                attachedPagosList.add(pagosListPagosToAttach);
            }
            tipodepago.setPagosList(attachedPagosList);
            List<Inscripcion> attachedInscripcionList = new ArrayList<Inscripcion>();
            for (Inscripcion inscripcionListInscripcionToAttach : tipodepago.getInscripcionList()) {
                inscripcionListInscripcionToAttach = em.getReference(inscripcionListInscripcionToAttach.getClass(), inscripcionListInscripcionToAttach.getIdInscripcion());
                attachedInscripcionList.add(inscripcionListInscripcionToAttach);
            }
            tipodepago.setInscripcionList(attachedInscripcionList);
            em.persist(tipodepago);
            for (Pagos pagosListPagos : tipodepago.getPagosList()) {
                Tipodepago oldIdTipoPagoOfPagosListPagos = pagosListPagos.getIdTipoPago();
                pagosListPagos.setIdTipoPago(tipodepago);
                pagosListPagos = em.merge(pagosListPagos);
                if (oldIdTipoPagoOfPagosListPagos != null) {
                    oldIdTipoPagoOfPagosListPagos.getPagosList().remove(pagosListPagos);
                    oldIdTipoPagoOfPagosListPagos = em.merge(oldIdTipoPagoOfPagosListPagos);
                }
            }
            for (Inscripcion inscripcionListInscripcion : tipodepago.getInscripcionList()) {
                Tipodepago oldIdTipoPagoOfInscripcionListInscripcion = inscripcionListInscripcion.getIdTipoPago();
                inscripcionListInscripcion.setIdTipoPago(tipodepago);
                inscripcionListInscripcion = em.merge(inscripcionListInscripcion);
                if (oldIdTipoPagoOfInscripcionListInscripcion != null) {
                    oldIdTipoPagoOfInscripcionListInscripcion.getInscripcionList().remove(inscripcionListInscripcion);
                    oldIdTipoPagoOfInscripcionListInscripcion = em.merge(oldIdTipoPagoOfInscripcionListInscripcion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipodepago tipodepago) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipodepago persistentTipodepago = em.find(Tipodepago.class, tipodepago.getIdTipoPago());
            List<Pagos> pagosListOld = persistentTipodepago.getPagosList();
            List<Pagos> pagosListNew = tipodepago.getPagosList();
            List<Inscripcion> inscripcionListOld = persistentTipodepago.getInscripcionList();
            List<Inscripcion> inscripcionListNew = tipodepago.getInscripcionList();
            List<String> illegalOrphanMessages = null;
            for (Pagos pagosListOldPagos : pagosListOld) {
                if (!pagosListNew.contains(pagosListOldPagos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pagos " + pagosListOldPagos + " since its idTipoPago field is not nullable.");
                }
            }
            for (Inscripcion inscripcionListOldInscripcion : inscripcionListOld) {
                if (!inscripcionListNew.contains(inscripcionListOldInscripcion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Inscripcion " + inscripcionListOldInscripcion + " since its idTipoPago field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Pagos> attachedPagosListNew = new ArrayList<Pagos>();
            for (Pagos pagosListNewPagosToAttach : pagosListNew) {
                pagosListNewPagosToAttach = em.getReference(pagosListNewPagosToAttach.getClass(), pagosListNewPagosToAttach.getIdPago());
                attachedPagosListNew.add(pagosListNewPagosToAttach);
            }
            pagosListNew = attachedPagosListNew;
            tipodepago.setPagosList(pagosListNew);
            List<Inscripcion> attachedInscripcionListNew = new ArrayList<Inscripcion>();
            for (Inscripcion inscripcionListNewInscripcionToAttach : inscripcionListNew) {
                inscripcionListNewInscripcionToAttach = em.getReference(inscripcionListNewInscripcionToAttach.getClass(), inscripcionListNewInscripcionToAttach.getIdInscripcion());
                attachedInscripcionListNew.add(inscripcionListNewInscripcionToAttach);
            }
            inscripcionListNew = attachedInscripcionListNew;
            tipodepago.setInscripcionList(inscripcionListNew);
            tipodepago = em.merge(tipodepago);
            for (Pagos pagosListNewPagos : pagosListNew) {
                if (!pagosListOld.contains(pagosListNewPagos)) {
                    Tipodepago oldIdTipoPagoOfPagosListNewPagos = pagosListNewPagos.getIdTipoPago();
                    pagosListNewPagos.setIdTipoPago(tipodepago);
                    pagosListNewPagos = em.merge(pagosListNewPagos);
                    if (oldIdTipoPagoOfPagosListNewPagos != null && !oldIdTipoPagoOfPagosListNewPagos.equals(tipodepago)) {
                        oldIdTipoPagoOfPagosListNewPagos.getPagosList().remove(pagosListNewPagos);
                        oldIdTipoPagoOfPagosListNewPagos = em.merge(oldIdTipoPagoOfPagosListNewPagos);
                    }
                }
            }
            for (Inscripcion inscripcionListNewInscripcion : inscripcionListNew) {
                if (!inscripcionListOld.contains(inscripcionListNewInscripcion)) {
                    Tipodepago oldIdTipoPagoOfInscripcionListNewInscripcion = inscripcionListNewInscripcion.getIdTipoPago();
                    inscripcionListNewInscripcion.setIdTipoPago(tipodepago);
                    inscripcionListNewInscripcion = em.merge(inscripcionListNewInscripcion);
                    if (oldIdTipoPagoOfInscripcionListNewInscripcion != null && !oldIdTipoPagoOfInscripcionListNewInscripcion.equals(tipodepago)) {
                        oldIdTipoPagoOfInscripcionListNewInscripcion.getInscripcionList().remove(inscripcionListNewInscripcion);
                        oldIdTipoPagoOfInscripcionListNewInscripcion = em.merge(oldIdTipoPagoOfInscripcionListNewInscripcion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipodepago.getIdTipoPago();
                if (findTipodepago(id) == null) {
                    throw new NonexistentEntityException("The tipodepago with id " + id + " no longer exists.");
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
            Tipodepago tipodepago;
            try {
                tipodepago = em.getReference(Tipodepago.class, id);
                tipodepago.getIdTipoPago();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipodepago with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Pagos> pagosListOrphanCheck = tipodepago.getPagosList();
            for (Pagos pagosListOrphanCheckPagos : pagosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tipodepago (" + tipodepago + ") cannot be destroyed since the Pagos " + pagosListOrphanCheckPagos + " in its pagosList field has a non-nullable idTipoPago field.");
            }
            List<Inscripcion> inscripcionListOrphanCheck = tipodepago.getInscripcionList();
            for (Inscripcion inscripcionListOrphanCheckInscripcion : inscripcionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tipodepago (" + tipodepago + ") cannot be destroyed since the Inscripcion " + inscripcionListOrphanCheckInscripcion + " in its inscripcionList field has a non-nullable idTipoPago field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipodepago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipodepago> findTipodepagoEntities() {
        return findTipodepagoEntities(true, -1, -1);
    }

    public List<Tipodepago> findTipodepagoEntities(int maxResults, int firstResult) {
        return findTipodepagoEntities(false, maxResults, firstResult);
    }

    private List<Tipodepago> findTipodepagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipodepago.class));
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

    public Tipodepago findTipodepago(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipodepago.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipodepagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipodepago> rt = cq.from(Tipodepago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
