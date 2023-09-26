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
import modelo.Alumno;
import modelo.Curso;
import modelo.Bimestre;
import modelo.Examen;

/**
 *
 * @author luismorales
 */
public class ExamenJpaController implements Serializable {

    public ExamenJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Examen examen) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno idAlumno = examen.getIdAlumno();
            if (idAlumno != null) {
                idAlumno = em.getReference(idAlumno.getClass(), idAlumno.getIdAlumno());
                examen.setIdAlumno(idAlumno);
            }
            Curso idCurso = examen.getIdCurso();
            if (idCurso != null) {
                idCurso = em.getReference(idCurso.getClass(), idCurso.getIdCurso());
                examen.setIdCurso(idCurso);
            }
            Bimestre idBimestre = examen.getIdBimestre();
            if (idBimestre != null) {
                idBimestre = em.getReference(idBimestre.getClass(), idBimestre.getIdBimestre());
                examen.setIdBimestre(idBimestre);
            }
            em.persist(examen);
            if (idAlumno != null) {
                idAlumno.getExamenList().add(examen);
                idAlumno = em.merge(idAlumno);
            }
            if (idCurso != null) {
                idCurso.getExamenList().add(examen);
                idCurso = em.merge(idCurso);
            }
            if (idBimestre != null) {
                idBimestre.getExamenList().add(examen);
                idBimestre = em.merge(idBimestre);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Examen examen) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Examen persistentExamen = em.find(Examen.class, examen.getIdExamen());
            Alumno idAlumnoOld = persistentExamen.getIdAlumno();
            Alumno idAlumnoNew = examen.getIdAlumno();
            Curso idCursoOld = persistentExamen.getIdCurso();
            Curso idCursoNew = examen.getIdCurso();
            Bimestre idBimestreOld = persistentExamen.getIdBimestre();
            Bimestre idBimestreNew = examen.getIdBimestre();
            if (idAlumnoNew != null) {
                idAlumnoNew = em.getReference(idAlumnoNew.getClass(), idAlumnoNew.getIdAlumno());
                examen.setIdAlumno(idAlumnoNew);
            }
            if (idCursoNew != null) {
                idCursoNew = em.getReference(idCursoNew.getClass(), idCursoNew.getIdCurso());
                examen.setIdCurso(idCursoNew);
            }
            if (idBimestreNew != null) {
                idBimestreNew = em.getReference(idBimestreNew.getClass(), idBimestreNew.getIdBimestre());
                examen.setIdBimestre(idBimestreNew);
            }
            examen = em.merge(examen);
            if (idAlumnoOld != null && !idAlumnoOld.equals(idAlumnoNew)) {
                idAlumnoOld.getExamenList().remove(examen);
                idAlumnoOld = em.merge(idAlumnoOld);
            }
            if (idAlumnoNew != null && !idAlumnoNew.equals(idAlumnoOld)) {
                idAlumnoNew.getExamenList().add(examen);
                idAlumnoNew = em.merge(idAlumnoNew);
            }
            if (idCursoOld != null && !idCursoOld.equals(idCursoNew)) {
                idCursoOld.getExamenList().remove(examen);
                idCursoOld = em.merge(idCursoOld);
            }
            if (idCursoNew != null && !idCursoNew.equals(idCursoOld)) {
                idCursoNew.getExamenList().add(examen);
                idCursoNew = em.merge(idCursoNew);
            }
            if (idBimestreOld != null && !idBimestreOld.equals(idBimestreNew)) {
                idBimestreOld.getExamenList().remove(examen);
                idBimestreOld = em.merge(idBimestreOld);
            }
            if (idBimestreNew != null && !idBimestreNew.equals(idBimestreOld)) {
                idBimestreNew.getExamenList().add(examen);
                idBimestreNew = em.merge(idBimestreNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = examen.getIdExamen();
                if (findExamen(id) == null) {
                    throw new NonexistentEntityException("The examen with id " + id + " no longer exists.");
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
            Examen examen;
            try {
                examen = em.getReference(Examen.class, id);
                examen.getIdExamen();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The examen with id " + id + " no longer exists.", enfe);
            }
            Alumno idAlumno = examen.getIdAlumno();
            if (idAlumno != null) {
                idAlumno.getExamenList().remove(examen);
                idAlumno = em.merge(idAlumno);
            }
            Curso idCurso = examen.getIdCurso();
            if (idCurso != null) {
                idCurso.getExamenList().remove(examen);
                idCurso = em.merge(idCurso);
            }
            Bimestre idBimestre = examen.getIdBimestre();
            if (idBimestre != null) {
                idBimestre.getExamenList().remove(examen);
                idBimestre = em.merge(idBimestre);
            }
            em.remove(examen);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Examen> findExamenEntities() {
        return findExamenEntities(true, -1, -1);
    }

    public List<Examen> findExamenEntities(int maxResults, int firstResult) {
        return findExamenEntities(false, maxResults, firstResult);
    }

    private List<Examen> findExamenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Examen.class));
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

    public Examen findExamen(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Examen.class, id);
        } finally {
            em.close();
        }
    }

    public int getExamenCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Examen> rt = cq.from(Examen.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
