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
import modelo.Documento;
import modelo.Documentosporpersona;
import modelo.Persona;

/**
 *
 * @author luismorales
 */
public class DocumentosporpersonaJpaController implements Serializable {

    public DocumentosporpersonaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Documentosporpersona documentosporpersona) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documento idDocumento = documentosporpersona.getIdDocumento();
            if (idDocumento != null) {
                idDocumento = em.getReference(idDocumento.getClass(), idDocumento.getIdDocumento());
                documentosporpersona.setIdDocumento(idDocumento);
            }
            Persona idPersona = documentosporpersona.getIdPersona();
            if (idPersona != null) {
                idPersona = em.getReference(idPersona.getClass(), idPersona.getIdPersona());
                documentosporpersona.setIdPersona(idPersona);
            }
            em.persist(documentosporpersona);
            if (idDocumento != null) {
                idDocumento.getDocumentosporpersonaList().add(documentosporpersona);
                idDocumento = em.merge(idDocumento);
            }
            if (idPersona != null) {
                idPersona.getDocumentosporpersonaList().add(documentosporpersona);
                idPersona = em.merge(idPersona);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Documentosporpersona documentosporpersona) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documentosporpersona persistentDocumentosporpersona = em.find(Documentosporpersona.class, documentosporpersona.getIdDocPorPersona());
            Documento idDocumentoOld = persistentDocumentosporpersona.getIdDocumento();
            Documento idDocumentoNew = documentosporpersona.getIdDocumento();
            Persona idPersonaOld = persistentDocumentosporpersona.getIdPersona();
            Persona idPersonaNew = documentosporpersona.getIdPersona();
            if (idDocumentoNew != null) {
                idDocumentoNew = em.getReference(idDocumentoNew.getClass(), idDocumentoNew.getIdDocumento());
                documentosporpersona.setIdDocumento(idDocumentoNew);
            }
            if (idPersonaNew != null) {
                idPersonaNew = em.getReference(idPersonaNew.getClass(), idPersonaNew.getIdPersona());
                documentosporpersona.setIdPersona(idPersonaNew);
            }
            documentosporpersona = em.merge(documentosporpersona);
            if (idDocumentoOld != null && !idDocumentoOld.equals(idDocumentoNew)) {
                idDocumentoOld.getDocumentosporpersonaList().remove(documentosporpersona);
                idDocumentoOld = em.merge(idDocumentoOld);
            }
            if (idDocumentoNew != null && !idDocumentoNew.equals(idDocumentoOld)) {
                idDocumentoNew.getDocumentosporpersonaList().add(documentosporpersona);
                idDocumentoNew = em.merge(idDocumentoNew);
            }
            if (idPersonaOld != null && !idPersonaOld.equals(idPersonaNew)) {
                idPersonaOld.getDocumentosporpersonaList().remove(documentosporpersona);
                idPersonaOld = em.merge(idPersonaOld);
            }
            if (idPersonaNew != null && !idPersonaNew.equals(idPersonaOld)) {
                idPersonaNew.getDocumentosporpersonaList().add(documentosporpersona);
                idPersonaNew = em.merge(idPersonaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = documentosporpersona.getIdDocPorPersona();
                if (findDocumentosporpersona(id) == null) {
                    throw new NonexistentEntityException("The documentosporpersona with id " + id + " no longer exists.");
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
            Documentosporpersona documentosporpersona;
            try {
                documentosporpersona = em.getReference(Documentosporpersona.class, id);
                documentosporpersona.getIdDocPorPersona();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The documentosporpersona with id " + id + " no longer exists.", enfe);
            }
            Documento idDocumento = documentosporpersona.getIdDocumento();
            if (idDocumento != null) {
                idDocumento.getDocumentosporpersonaList().remove(documentosporpersona);
                idDocumento = em.merge(idDocumento);
            }
            Persona idPersona = documentosporpersona.getIdPersona();
            if (idPersona != null) {
                idPersona.getDocumentosporpersonaList().remove(documentosporpersona);
                idPersona = em.merge(idPersona);
            }
            em.remove(documentosporpersona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Documentosporpersona> findDocumentosporpersonaEntities() {
        return findDocumentosporpersonaEntities(true, -1, -1);
    }

    public List<Documentosporpersona> findDocumentosporpersonaEntities(int maxResults, int firstResult) {
        return findDocumentosporpersonaEntities(false, maxResults, firstResult);
    }

    private List<Documentosporpersona> findDocumentosporpersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Documentosporpersona.class));
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

    public Documentosporpersona findDocumentosporpersona(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Documentosporpersona.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocumentosporpersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Documentosporpersona> rt = cq.from(Documentosporpersona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
