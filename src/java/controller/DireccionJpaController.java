/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Departamento;
import modelo.Direccion;
import modelo.Municipio;
import modelo.Persona;

/**
 *
 * @author luismorales
 */
public class DireccionJpaController implements Serializable {

    public DireccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Direccion direccion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento idDepartamento = direccion.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento = em.getReference(idDepartamento.getClass(), idDepartamento.getIdDepartamento());
                direccion.setIdDepartamento(idDepartamento);
            }
            Municipio idMunicipio = direccion.getIdMunicipio();
            if (idMunicipio != null) {
                idMunicipio = em.getReference(idMunicipio.getClass(), idMunicipio.getIdMunicipio());
                direccion.setIdMunicipio(idMunicipio);
            }
            Persona idPersona = direccion.getIdPersona();
            if (idPersona != null) {
                idPersona = em.getReference(idPersona.getClass(), idPersona.getIdPersona());
                direccion.setIdPersona(idPersona);
            }
            em.persist(direccion);
            if (idDepartamento != null) {
                idDepartamento.getDireccionList().add(direccion);
                idDepartamento = em.merge(idDepartamento);
            }
            if (idMunicipio != null) {
                idMunicipio.getDireccionList().add(direccion);
                idMunicipio = em.merge(idMunicipio);
            }
            if (idPersona != null) {
                idPersona.getDireccionList().add(direccion);
                idPersona = em.merge(idPersona);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Direccion direccion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion persistentDireccion = em.find(Direccion.class, direccion.getIdDireccion());
            Departamento idDepartamentoOld = persistentDireccion.getIdDepartamento();
            Departamento idDepartamentoNew = direccion.getIdDepartamento();
            Municipio idMunicipioOld = persistentDireccion.getIdMunicipio();
            Municipio idMunicipioNew = direccion.getIdMunicipio();
            Persona idPersonaOld = persistentDireccion.getIdPersona();
            Persona idPersonaNew = direccion.getIdPersona();
            if (idDepartamentoNew != null) {
                idDepartamentoNew = em.getReference(idDepartamentoNew.getClass(), idDepartamentoNew.getIdDepartamento());
                direccion.setIdDepartamento(idDepartamentoNew);
            }
            if (idMunicipioNew != null) {
                idMunicipioNew = em.getReference(idMunicipioNew.getClass(), idMunicipioNew.getIdMunicipio());
                direccion.setIdMunicipio(idMunicipioNew);
            }
            if (idPersonaNew != null) {
                idPersonaNew = em.getReference(idPersonaNew.getClass(), idPersonaNew.getIdPersona());
                direccion.setIdPersona(idPersonaNew);
            }
            direccion = em.merge(direccion);
            if (idDepartamentoOld != null && !idDepartamentoOld.equals(idDepartamentoNew)) {
                idDepartamentoOld.getDireccionList().remove(direccion);
                idDepartamentoOld = em.merge(idDepartamentoOld);
            }
            if (idDepartamentoNew != null && !idDepartamentoNew.equals(idDepartamentoOld)) {
                idDepartamentoNew.getDireccionList().add(direccion);
                idDepartamentoNew = em.merge(idDepartamentoNew);
            }
            if (idMunicipioOld != null && !idMunicipioOld.equals(idMunicipioNew)) {
                idMunicipioOld.getDireccionList().remove(direccion);
                idMunicipioOld = em.merge(idMunicipioOld);
            }
            if (idMunicipioNew != null && !idMunicipioNew.equals(idMunicipioOld)) {
                idMunicipioNew.getDireccionList().add(direccion);
                idMunicipioNew = em.merge(idMunicipioNew);
            }
            if (idPersonaOld != null && !idPersonaOld.equals(idPersonaNew)) {
                idPersonaOld.getDireccionList().remove(direccion);
                idPersonaOld = em.merge(idPersonaOld);
            }
            if (idPersonaNew != null && !idPersonaNew.equals(idPersonaOld)) {
                idPersonaNew.getDireccionList().add(direccion);
                idPersonaNew = em.merge(idPersonaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = direccion.getIdDireccion();
                if (findDireccion(id) == null) {
                    throw new NonexistentEntityException("The direccion with id " + id + " no longer exists.");
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
            Direccion direccion;
            try {
                direccion = em.getReference(Direccion.class, id);
                direccion.getIdDireccion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The direccion with id " + id + " no longer exists.", enfe);
            }
            Departamento idDepartamento = direccion.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento.getDireccionList().remove(direccion);
                idDepartamento = em.merge(idDepartamento);
            }
            Municipio idMunicipio = direccion.getIdMunicipio();
            if (idMunicipio != null) {
                idMunicipio.getDireccionList().remove(direccion);
                idMunicipio = em.merge(idMunicipio);
            }
            Persona idPersona = direccion.getIdPersona();
            if (idPersona != null) {
                idPersona.getDireccionList().remove(direccion);
                idPersona = em.merge(idPersona);
            }
            em.remove(direccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Direccion> findDireccionEntities() {
        return findDireccionEntities(true, -1, -1);
    }

    public List<Direccion> findDireccionEntities(int maxResults, int firstResult) {
        return findDireccionEntities(false, maxResults, firstResult);
    }

    private List<Direccion> findDireccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Direccion.class));
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

    public Direccion findDireccion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Direccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getDireccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Direccion> rt = cq.from(Direccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
