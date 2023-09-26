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
import modelo.Nota;

/**
 *
 * @author luismorales
 */
public class NotaJpaController implements Serializable {

    public NotaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Nota nota) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno idAlumno = nota.getIdAlumno();
            if (idAlumno != null) {
                idAlumno = em.getReference(idAlumno.getClass(), idAlumno.getIdAlumno());
                nota.setIdAlumno(idAlumno);
            }
            Curso idCurso = nota.getIdCurso();
            if (idCurso != null) {
                idCurso = em.getReference(idCurso.getClass(), idCurso.getIdCurso());
                nota.setIdCurso(idCurso);
            }
            Bimestre idBimestre = nota.getIdBimestre();
            if (idBimestre != null) {
                idBimestre = em.getReference(idBimestre.getClass(), idBimestre.getIdBimestre());
                nota.setIdBimestre(idBimestre);
            }
            em.persist(nota);
            if (idAlumno != null) {
                idAlumno.getNotaList().add(nota);
                idAlumno = em.merge(idAlumno);
            }
            if (idCurso != null) {
                idCurso.getNotaList().add(nota);
                idCurso = em.merge(idCurso);
            }
            if (idBimestre != null) {
                idBimestre.getNotaList().add(nota);
                idBimestre = em.merge(idBimestre);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Nota nota) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Nota persistentNota = em.find(Nota.class, nota.getIdNota());
            Alumno idAlumnoOld = persistentNota.getIdAlumno();
            Alumno idAlumnoNew = nota.getIdAlumno();
            Curso idCursoOld = persistentNota.getIdCurso();
            Curso idCursoNew = nota.getIdCurso();
            Bimestre idBimestreOld = persistentNota.getIdBimestre();
            Bimestre idBimestreNew = nota.getIdBimestre();
            if (idAlumnoNew != null) {
                idAlumnoNew = em.getReference(idAlumnoNew.getClass(), idAlumnoNew.getIdAlumno());
                nota.setIdAlumno(idAlumnoNew);
            }
            if (idCursoNew != null) {
                idCursoNew = em.getReference(idCursoNew.getClass(), idCursoNew.getIdCurso());
                nota.setIdCurso(idCursoNew);
            }
            if (idBimestreNew != null) {
                idBimestreNew = em.getReference(idBimestreNew.getClass(), idBimestreNew.getIdBimestre());
                nota.setIdBimestre(idBimestreNew);
            }
            nota = em.merge(nota);
            if (idAlumnoOld != null && !idAlumnoOld.equals(idAlumnoNew)) {
                idAlumnoOld.getNotaList().remove(nota);
                idAlumnoOld = em.merge(idAlumnoOld);
            }
            if (idAlumnoNew != null && !idAlumnoNew.equals(idAlumnoOld)) {
                idAlumnoNew.getNotaList().add(nota);
                idAlumnoNew = em.merge(idAlumnoNew);
            }
            if (idCursoOld != null && !idCursoOld.equals(idCursoNew)) {
                idCursoOld.getNotaList().remove(nota);
                idCursoOld = em.merge(idCursoOld);
            }
            if (idCursoNew != null && !idCursoNew.equals(idCursoOld)) {
                idCursoNew.getNotaList().add(nota);
                idCursoNew = em.merge(idCursoNew);
            }
            if (idBimestreOld != null && !idBimestreOld.equals(idBimestreNew)) {
                idBimestreOld.getNotaList().remove(nota);
                idBimestreOld = em.merge(idBimestreOld);
            }
            if (idBimestreNew != null && !idBimestreNew.equals(idBimestreOld)) {
                idBimestreNew.getNotaList().add(nota);
                idBimestreNew = em.merge(idBimestreNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = nota.getIdNota();
                if (findNota(id) == null) {
                    throw new NonexistentEntityException("The nota with id " + id + " no longer exists.");
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
            Nota nota;
            try {
                nota = em.getReference(Nota.class, id);
                nota.getIdNota();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The nota with id " + id + " no longer exists.", enfe);
            }
            Alumno idAlumno = nota.getIdAlumno();
            if (idAlumno != null) {
                idAlumno.getNotaList().remove(nota);
                idAlumno = em.merge(idAlumno);
            }
            Curso idCurso = nota.getIdCurso();
            if (idCurso != null) {
                idCurso.getNotaList().remove(nota);
                idCurso = em.merge(idCurso);
            }
            Bimestre idBimestre = nota.getIdBimestre();
            if (idBimestre != null) {
                idBimestre.getNotaList().remove(nota);
                idBimestre = em.merge(idBimestre);
            }
            em.remove(nota);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Nota> findNotaEntities() {
        return findNotaEntities(true, -1, -1);
    }

    public List<Nota> findNotaEntities(int maxResults, int firstResult) {
        return findNotaEntities(false, maxResults, firstResult);
    }

    private List<Nota> findNotaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Nota.class));
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

    public Nota findNota(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Nota.class, id);
        } finally {
            em.close();
        }
    }

    public int getNotaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Nota> rt = cq.from(Nota.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
