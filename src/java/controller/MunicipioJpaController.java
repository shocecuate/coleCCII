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
import modelo.Departamento;
import modelo.Direccion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Municipio;

/**
 *
 * @author luismorales
 */
public class MunicipioJpaController implements Serializable {

    public MunicipioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Municipio municipio) {
        if (municipio.getDireccionList() == null) {
            municipio.setDireccionList(new ArrayList<Direccion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento idDepartamento = municipio.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento = em.getReference(idDepartamento.getClass(), idDepartamento.getIdDepartamento());
                municipio.setIdDepartamento(idDepartamento);
            }
            List<Direccion> attachedDireccionList = new ArrayList<Direccion>();
            for (Direccion direccionListDireccionToAttach : municipio.getDireccionList()) {
                direccionListDireccionToAttach = em.getReference(direccionListDireccionToAttach.getClass(), direccionListDireccionToAttach.getIdDireccion());
                attachedDireccionList.add(direccionListDireccionToAttach);
            }
            municipio.setDireccionList(attachedDireccionList);
            em.persist(municipio);
            if (idDepartamento != null) {
                idDepartamento.getMunicipioList().add(municipio);
                idDepartamento = em.merge(idDepartamento);
            }
            for (Direccion direccionListDireccion : municipio.getDireccionList()) {
                Municipio oldIdMunicipioOfDireccionListDireccion = direccionListDireccion.getIdMunicipio();
                direccionListDireccion.setIdMunicipio(municipio);
                direccionListDireccion = em.merge(direccionListDireccion);
                if (oldIdMunicipioOfDireccionListDireccion != null) {
                    oldIdMunicipioOfDireccionListDireccion.getDireccionList().remove(direccionListDireccion);
                    oldIdMunicipioOfDireccionListDireccion = em.merge(oldIdMunicipioOfDireccionListDireccion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Municipio municipio) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Municipio persistentMunicipio = em.find(Municipio.class, municipio.getIdMunicipio());
            Departamento idDepartamentoOld = persistentMunicipio.getIdDepartamento();
            Departamento idDepartamentoNew = municipio.getIdDepartamento();
            List<Direccion> direccionListOld = persistentMunicipio.getDireccionList();
            List<Direccion> direccionListNew = municipio.getDireccionList();
            List<String> illegalOrphanMessages = null;
            for (Direccion direccionListOldDireccion : direccionListOld) {
                if (!direccionListNew.contains(direccionListOldDireccion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Direccion " + direccionListOldDireccion + " since its idMunicipio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idDepartamentoNew != null) {
                idDepartamentoNew = em.getReference(idDepartamentoNew.getClass(), idDepartamentoNew.getIdDepartamento());
                municipio.setIdDepartamento(idDepartamentoNew);
            }
            List<Direccion> attachedDireccionListNew = new ArrayList<Direccion>();
            for (Direccion direccionListNewDireccionToAttach : direccionListNew) {
                direccionListNewDireccionToAttach = em.getReference(direccionListNewDireccionToAttach.getClass(), direccionListNewDireccionToAttach.getIdDireccion());
                attachedDireccionListNew.add(direccionListNewDireccionToAttach);
            }
            direccionListNew = attachedDireccionListNew;
            municipio.setDireccionList(direccionListNew);
            municipio = em.merge(municipio);
            if (idDepartamentoOld != null && !idDepartamentoOld.equals(idDepartamentoNew)) {
                idDepartamentoOld.getMunicipioList().remove(municipio);
                idDepartamentoOld = em.merge(idDepartamentoOld);
            }
            if (idDepartamentoNew != null && !idDepartamentoNew.equals(idDepartamentoOld)) {
                idDepartamentoNew.getMunicipioList().add(municipio);
                idDepartamentoNew = em.merge(idDepartamentoNew);
            }
            for (Direccion direccionListNewDireccion : direccionListNew) {
                if (!direccionListOld.contains(direccionListNewDireccion)) {
                    Municipio oldIdMunicipioOfDireccionListNewDireccion = direccionListNewDireccion.getIdMunicipio();
                    direccionListNewDireccion.setIdMunicipio(municipio);
                    direccionListNewDireccion = em.merge(direccionListNewDireccion);
                    if (oldIdMunicipioOfDireccionListNewDireccion != null && !oldIdMunicipioOfDireccionListNewDireccion.equals(municipio)) {
                        oldIdMunicipioOfDireccionListNewDireccion.getDireccionList().remove(direccionListNewDireccion);
                        oldIdMunicipioOfDireccionListNewDireccion = em.merge(oldIdMunicipioOfDireccionListNewDireccion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = municipio.getIdMunicipio();
                if (findMunicipio(id) == null) {
                    throw new NonexistentEntityException("The municipio with id " + id + " no longer exists.");
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
            Municipio municipio;
            try {
                municipio = em.getReference(Municipio.class, id);
                municipio.getIdMunicipio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The municipio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Direccion> direccionListOrphanCheck = municipio.getDireccionList();
            for (Direccion direccionListOrphanCheckDireccion : direccionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Municipio (" + municipio + ") cannot be destroyed since the Direccion " + direccionListOrphanCheckDireccion + " in its direccionList field has a non-nullable idMunicipio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Departamento idDepartamento = municipio.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento.getMunicipioList().remove(municipio);
                idDepartamento = em.merge(idDepartamento);
            }
            em.remove(municipio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Municipio> findMunicipioEntities() {
        return findMunicipioEntities(true, -1, -1);
    }

    public List<Municipio> findMunicipioEntities(int maxResults, int firstResult) {
        return findMunicipioEntities(false, maxResults, firstResult);
    }

    private List<Municipio> findMunicipioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Municipio.class));
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

    public Municipio findMunicipio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Municipio.class, id);
        } finally {
            em.close();
        }
    }

    public int getMunicipioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Municipio> rt = cq.from(Municipio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
