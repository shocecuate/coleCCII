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
import modelo.Documentosporpersona;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Documento;

/**
 *
 * @author luismorales
 */
public class DocumentoJpaController implements Serializable {

    public DocumentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Documento documento) {
        if (documento.getDocumentosporpersonaList() == null) {
            documento.setDocumentosporpersonaList(new ArrayList<Documentosporpersona>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Documentosporpersona> attachedDocumentosporpersonaList = new ArrayList<Documentosporpersona>();
            for (Documentosporpersona documentosporpersonaListDocumentosporpersonaToAttach : documento.getDocumentosporpersonaList()) {
                documentosporpersonaListDocumentosporpersonaToAttach = em.getReference(documentosporpersonaListDocumentosporpersonaToAttach.getClass(), documentosporpersonaListDocumentosporpersonaToAttach.getIdDocPorPersona());
                attachedDocumentosporpersonaList.add(documentosporpersonaListDocumentosporpersonaToAttach);
            }
            documento.setDocumentosporpersonaList(attachedDocumentosporpersonaList);
            em.persist(documento);
            for (Documentosporpersona documentosporpersonaListDocumentosporpersona : documento.getDocumentosporpersonaList()) {
                Documento oldIdDocumentoOfDocumentosporpersonaListDocumentosporpersona = documentosporpersonaListDocumentosporpersona.getIdDocumento();
                documentosporpersonaListDocumentosporpersona.setIdDocumento(documento);
                documentosporpersonaListDocumentosporpersona = em.merge(documentosporpersonaListDocumentosporpersona);
                if (oldIdDocumentoOfDocumentosporpersonaListDocumentosporpersona != null) {
                    oldIdDocumentoOfDocumentosporpersonaListDocumentosporpersona.getDocumentosporpersonaList().remove(documentosporpersonaListDocumentosporpersona);
                    oldIdDocumentoOfDocumentosporpersonaListDocumentosporpersona = em.merge(oldIdDocumentoOfDocumentosporpersonaListDocumentosporpersona);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Documento documento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documento persistentDocumento = em.find(Documento.class, documento.getIdDocumento());
            List<Documentosporpersona> documentosporpersonaListOld = persistentDocumento.getDocumentosporpersonaList();
            List<Documentosporpersona> documentosporpersonaListNew = documento.getDocumentosporpersonaList();
            List<String> illegalOrphanMessages = null;
            for (Documentosporpersona documentosporpersonaListOldDocumentosporpersona : documentosporpersonaListOld) {
                if (!documentosporpersonaListNew.contains(documentosporpersonaListOldDocumentosporpersona)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Documentosporpersona " + documentosporpersonaListOldDocumentosporpersona + " since its idDocumento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Documentosporpersona> attachedDocumentosporpersonaListNew = new ArrayList<Documentosporpersona>();
            for (Documentosporpersona documentosporpersonaListNewDocumentosporpersonaToAttach : documentosporpersonaListNew) {
                documentosporpersonaListNewDocumentosporpersonaToAttach = em.getReference(documentosporpersonaListNewDocumentosporpersonaToAttach.getClass(), documentosporpersonaListNewDocumentosporpersonaToAttach.getIdDocPorPersona());
                attachedDocumentosporpersonaListNew.add(documentosporpersonaListNewDocumentosporpersonaToAttach);
            }
            documentosporpersonaListNew = attachedDocumentosporpersonaListNew;
            documento.setDocumentosporpersonaList(documentosporpersonaListNew);
            documento = em.merge(documento);
            for (Documentosporpersona documentosporpersonaListNewDocumentosporpersona : documentosporpersonaListNew) {
                if (!documentosporpersonaListOld.contains(documentosporpersonaListNewDocumentosporpersona)) {
                    Documento oldIdDocumentoOfDocumentosporpersonaListNewDocumentosporpersona = documentosporpersonaListNewDocumentosporpersona.getIdDocumento();
                    documentosporpersonaListNewDocumentosporpersona.setIdDocumento(documento);
                    documentosporpersonaListNewDocumentosporpersona = em.merge(documentosporpersonaListNewDocumentosporpersona);
                    if (oldIdDocumentoOfDocumentosporpersonaListNewDocumentosporpersona != null && !oldIdDocumentoOfDocumentosporpersonaListNewDocumentosporpersona.equals(documento)) {
                        oldIdDocumentoOfDocumentosporpersonaListNewDocumentosporpersona.getDocumentosporpersonaList().remove(documentosporpersonaListNewDocumentosporpersona);
                        oldIdDocumentoOfDocumentosporpersonaListNewDocumentosporpersona = em.merge(oldIdDocumentoOfDocumentosporpersonaListNewDocumentosporpersona);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = documento.getIdDocumento();
                if (findDocumento(id) == null) {
                    throw new NonexistentEntityException("The documento with id " + id + " no longer exists.");
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
            Documento documento;
            try {
                documento = em.getReference(Documento.class, id);
                documento.getIdDocumento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The documento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Documentosporpersona> documentosporpersonaListOrphanCheck = documento.getDocumentosporpersonaList();
            for (Documentosporpersona documentosporpersonaListOrphanCheckDocumentosporpersona : documentosporpersonaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Documento (" + documento + ") cannot be destroyed since the Documentosporpersona " + documentosporpersonaListOrphanCheckDocumentosporpersona + " in its documentosporpersonaList field has a non-nullable idDocumento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(documento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Documento> findDocumentoEntities() {
        return findDocumentoEntities(true, -1, -1);
    }

    public List<Documento> findDocumentoEntities(int maxResults, int firstResult) {
        return findDocumentoEntities(false, maxResults, firstResult);
    }

    private List<Documento> findDocumentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Documento.class));
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

    public Documento findDocumento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Documento.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocumentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Documento> rt = cq.from(Documento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
