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
import modelo.Asignacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Profesor;

/**
 *
 * @author luismorales
 */
public class ProfesorJpaController implements Serializable {

    public ProfesorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Profesor profesor) {
        if (profesor.getAsignacionList() == null) {
            profesor.setAsignacionList(new ArrayList<Asignacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona idPersona = profesor.getIdPersona();
            if (idPersona != null) {
                idPersona = em.getReference(idPersona.getClass(), idPersona.getIdPersona());
                profesor.setIdPersona(idPersona);
            }
            List<Asignacion> attachedAsignacionList = new ArrayList<Asignacion>();
            for (Asignacion asignacionListAsignacionToAttach : profesor.getAsignacionList()) {
                asignacionListAsignacionToAttach = em.getReference(asignacionListAsignacionToAttach.getClass(), asignacionListAsignacionToAttach.getIdAsignacion());
                attachedAsignacionList.add(asignacionListAsignacionToAttach);
            }
            profesor.setAsignacionList(attachedAsignacionList);
            em.persist(profesor);
            if (idPersona != null) {
                idPersona.getProfesorList().add(profesor);
                idPersona = em.merge(idPersona);
            }
            for (Asignacion asignacionListAsignacion : profesor.getAsignacionList()) {
                Profesor oldIdProfesorOfAsignacionListAsignacion = asignacionListAsignacion.getIdProfesor();
                asignacionListAsignacion.setIdProfesor(profesor);
                asignacionListAsignacion = em.merge(asignacionListAsignacion);
                if (oldIdProfesorOfAsignacionListAsignacion != null) {
                    oldIdProfesorOfAsignacionListAsignacion.getAsignacionList().remove(asignacionListAsignacion);
                    oldIdProfesorOfAsignacionListAsignacion = em.merge(oldIdProfesorOfAsignacionListAsignacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Profesor profesor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profesor persistentProfesor = em.find(Profesor.class, profesor.getIdProfesor());
            Persona idPersonaOld = persistentProfesor.getIdPersona();
            Persona idPersonaNew = profesor.getIdPersona();
            List<Asignacion> asignacionListOld = persistentProfesor.getAsignacionList();
            List<Asignacion> asignacionListNew = profesor.getAsignacionList();
            List<String> illegalOrphanMessages = null;
            for (Asignacion asignacionListOldAsignacion : asignacionListOld) {
                if (!asignacionListNew.contains(asignacionListOldAsignacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Asignacion " + asignacionListOldAsignacion + " since its idProfesor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPersonaNew != null) {
                idPersonaNew = em.getReference(idPersonaNew.getClass(), idPersonaNew.getIdPersona());
                profesor.setIdPersona(idPersonaNew);
            }
            List<Asignacion> attachedAsignacionListNew = new ArrayList<Asignacion>();
            for (Asignacion asignacionListNewAsignacionToAttach : asignacionListNew) {
                asignacionListNewAsignacionToAttach = em.getReference(asignacionListNewAsignacionToAttach.getClass(), asignacionListNewAsignacionToAttach.getIdAsignacion());
                attachedAsignacionListNew.add(asignacionListNewAsignacionToAttach);
            }
            asignacionListNew = attachedAsignacionListNew;
            profesor.setAsignacionList(asignacionListNew);
            profesor = em.merge(profesor);
            if (idPersonaOld != null && !idPersonaOld.equals(idPersonaNew)) {
                idPersonaOld.getProfesorList().remove(profesor);
                idPersonaOld = em.merge(idPersonaOld);
            }
            if (idPersonaNew != null && !idPersonaNew.equals(idPersonaOld)) {
                idPersonaNew.getProfesorList().add(profesor);
                idPersonaNew = em.merge(idPersonaNew);
            }
            for (Asignacion asignacionListNewAsignacion : asignacionListNew) {
                if (!asignacionListOld.contains(asignacionListNewAsignacion)) {
                    Profesor oldIdProfesorOfAsignacionListNewAsignacion = asignacionListNewAsignacion.getIdProfesor();
                    asignacionListNewAsignacion.setIdProfesor(profesor);
                    asignacionListNewAsignacion = em.merge(asignacionListNewAsignacion);
                    if (oldIdProfesorOfAsignacionListNewAsignacion != null && !oldIdProfesorOfAsignacionListNewAsignacion.equals(profesor)) {
                        oldIdProfesorOfAsignacionListNewAsignacion.getAsignacionList().remove(asignacionListNewAsignacion);
                        oldIdProfesorOfAsignacionListNewAsignacion = em.merge(oldIdProfesorOfAsignacionListNewAsignacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = profesor.getIdProfesor();
                if (findProfesor(id) == null) {
                    throw new NonexistentEntityException("The profesor with id " + id + " no longer exists.");
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
            Profesor profesor;
            try {
                profesor = em.getReference(Profesor.class, id);
                profesor.getIdProfesor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profesor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Asignacion> asignacionListOrphanCheck = profesor.getAsignacionList();
            for (Asignacion asignacionListOrphanCheckAsignacion : asignacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Profesor (" + profesor + ") cannot be destroyed since the Asignacion " + asignacionListOrphanCheckAsignacion + " in its asignacionList field has a non-nullable idProfesor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Persona idPersona = profesor.getIdPersona();
            if (idPersona != null) {
                idPersona.getProfesorList().remove(profesor);
                idPersona = em.merge(idPersona);
            }
            em.remove(profesor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Profesor> findProfesorEntities() {
        return findProfesorEntities(true, -1, -1);
    }

    public List<Profesor> findProfesorEntities(int maxResults, int firstResult) {
        return findProfesorEntities(false, maxResults, firstResult);
    }

    private List<Profesor> findProfesorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Profesor.class));
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

    public Profesor findProfesor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Profesor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfesorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Profesor> rt = cq.from(Profesor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
