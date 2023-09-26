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
import modelo.Municipio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Departamento;
import modelo.Direccion;

/**
 *
 * @author luismorales
 */
public class DepartamentoJpaController implements Serializable {

    public DepartamentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Departamento departamento) {
        if (departamento.getMunicipioList() == null) {
            departamento.setMunicipioList(new ArrayList<Municipio>());
        }
        if (departamento.getDireccionList() == null) {
            departamento.setDireccionList(new ArrayList<Direccion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Municipio> attachedMunicipioList = new ArrayList<Municipio>();
            for (Municipio municipioListMunicipioToAttach : departamento.getMunicipioList()) {
                municipioListMunicipioToAttach = em.getReference(municipioListMunicipioToAttach.getClass(), municipioListMunicipioToAttach.getIdMunicipio());
                attachedMunicipioList.add(municipioListMunicipioToAttach);
            }
            departamento.setMunicipioList(attachedMunicipioList);
            List<Direccion> attachedDireccionList = new ArrayList<Direccion>();
            for (Direccion direccionListDireccionToAttach : departamento.getDireccionList()) {
                direccionListDireccionToAttach = em.getReference(direccionListDireccionToAttach.getClass(), direccionListDireccionToAttach.getIdDireccion());
                attachedDireccionList.add(direccionListDireccionToAttach);
            }
            departamento.setDireccionList(attachedDireccionList);
            em.persist(departamento);
            for (Municipio municipioListMunicipio : departamento.getMunicipioList()) {
                Departamento oldIdDepartamentoOfMunicipioListMunicipio = municipioListMunicipio.getIdDepartamento();
                municipioListMunicipio.setIdDepartamento(departamento);
                municipioListMunicipio = em.merge(municipioListMunicipio);
                if (oldIdDepartamentoOfMunicipioListMunicipio != null) {
                    oldIdDepartamentoOfMunicipioListMunicipio.getMunicipioList().remove(municipioListMunicipio);
                    oldIdDepartamentoOfMunicipioListMunicipio = em.merge(oldIdDepartamentoOfMunicipioListMunicipio);
                }
            }
            for (Direccion direccionListDireccion : departamento.getDireccionList()) {
                Departamento oldIdDepartamentoOfDireccionListDireccion = direccionListDireccion.getIdDepartamento();
                direccionListDireccion.setIdDepartamento(departamento);
                direccionListDireccion = em.merge(direccionListDireccion);
                if (oldIdDepartamentoOfDireccionListDireccion != null) {
                    oldIdDepartamentoOfDireccionListDireccion.getDireccionList().remove(direccionListDireccion);
                    oldIdDepartamentoOfDireccionListDireccion = em.merge(oldIdDepartamentoOfDireccionListDireccion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Departamento departamento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento persistentDepartamento = em.find(Departamento.class, departamento.getIdDepartamento());
            List<Municipio> municipioListOld = persistentDepartamento.getMunicipioList();
            List<Municipio> municipioListNew = departamento.getMunicipioList();
            List<Direccion> direccionListOld = persistentDepartamento.getDireccionList();
            List<Direccion> direccionListNew = departamento.getDireccionList();
            List<String> illegalOrphanMessages = null;
            for (Municipio municipioListOldMunicipio : municipioListOld) {
                if (!municipioListNew.contains(municipioListOldMunicipio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Municipio " + municipioListOldMunicipio + " since its idDepartamento field is not nullable.");
                }
            }
            for (Direccion direccionListOldDireccion : direccionListOld) {
                if (!direccionListNew.contains(direccionListOldDireccion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Direccion " + direccionListOldDireccion + " since its idDepartamento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Municipio> attachedMunicipioListNew = new ArrayList<Municipio>();
            for (Municipio municipioListNewMunicipioToAttach : municipioListNew) {
                municipioListNewMunicipioToAttach = em.getReference(municipioListNewMunicipioToAttach.getClass(), municipioListNewMunicipioToAttach.getIdMunicipio());
                attachedMunicipioListNew.add(municipioListNewMunicipioToAttach);
            }
            municipioListNew = attachedMunicipioListNew;
            departamento.setMunicipioList(municipioListNew);
            List<Direccion> attachedDireccionListNew = new ArrayList<Direccion>();
            for (Direccion direccionListNewDireccionToAttach : direccionListNew) {
                direccionListNewDireccionToAttach = em.getReference(direccionListNewDireccionToAttach.getClass(), direccionListNewDireccionToAttach.getIdDireccion());
                attachedDireccionListNew.add(direccionListNewDireccionToAttach);
            }
            direccionListNew = attachedDireccionListNew;
            departamento.setDireccionList(direccionListNew);
            departamento = em.merge(departamento);
            for (Municipio municipioListNewMunicipio : municipioListNew) {
                if (!municipioListOld.contains(municipioListNewMunicipio)) {
                    Departamento oldIdDepartamentoOfMunicipioListNewMunicipio = municipioListNewMunicipio.getIdDepartamento();
                    municipioListNewMunicipio.setIdDepartamento(departamento);
                    municipioListNewMunicipio = em.merge(municipioListNewMunicipio);
                    if (oldIdDepartamentoOfMunicipioListNewMunicipio != null && !oldIdDepartamentoOfMunicipioListNewMunicipio.equals(departamento)) {
                        oldIdDepartamentoOfMunicipioListNewMunicipio.getMunicipioList().remove(municipioListNewMunicipio);
                        oldIdDepartamentoOfMunicipioListNewMunicipio = em.merge(oldIdDepartamentoOfMunicipioListNewMunicipio);
                    }
                }
            }
            for (Direccion direccionListNewDireccion : direccionListNew) {
                if (!direccionListOld.contains(direccionListNewDireccion)) {
                    Departamento oldIdDepartamentoOfDireccionListNewDireccion = direccionListNewDireccion.getIdDepartamento();
                    direccionListNewDireccion.setIdDepartamento(departamento);
                    direccionListNewDireccion = em.merge(direccionListNewDireccion);
                    if (oldIdDepartamentoOfDireccionListNewDireccion != null && !oldIdDepartamentoOfDireccionListNewDireccion.equals(departamento)) {
                        oldIdDepartamentoOfDireccionListNewDireccion.getDireccionList().remove(direccionListNewDireccion);
                        oldIdDepartamentoOfDireccionListNewDireccion = em.merge(oldIdDepartamentoOfDireccionListNewDireccion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = departamento.getIdDepartamento();
                if (findDepartamento(id) == null) {
                    throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.");
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
            Departamento departamento;
            try {
                departamento = em.getReference(Departamento.class, id);
                departamento.getIdDepartamento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Municipio> municipioListOrphanCheck = departamento.getMunicipioList();
            for (Municipio municipioListOrphanCheckMunicipio : municipioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Municipio " + municipioListOrphanCheckMunicipio + " in its municipioList field has a non-nullable idDepartamento field.");
            }
            List<Direccion> direccionListOrphanCheck = departamento.getDireccionList();
            for (Direccion direccionListOrphanCheckDireccion : direccionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Direccion " + direccionListOrphanCheckDireccion + " in its direccionList field has a non-nullable idDepartamento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(departamento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Departamento> findDepartamentoEntities() {
        return findDepartamentoEntities(true, -1, -1);
    }

    public List<Departamento> findDepartamentoEntities(int maxResults, int firstResult) {
        return findDepartamentoEntities(false, maxResults, firstResult);
    }

    private List<Departamento> findDepartamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Departamento.class));
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

    public Departamento findDepartamento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Departamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Departamento> rt = cq.from(Departamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
