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
import modelo.Usuario;
import modelo.Curso;
import modelo.Materia;
import modelo.Materiacursoporusuario;

/**
 *
 * @author luismorales
 */
public class MateriacursoporusuarioJpaController implements Serializable {

    public MateriacursoporusuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Materiacursoporusuario materiacursoporusuario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario idUsuario = materiacursoporusuario.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                materiacursoporusuario.setIdUsuario(idUsuario);
            }
            Curso idCurso = materiacursoporusuario.getIdCurso();
            if (idCurso != null) {
                idCurso = em.getReference(idCurso.getClass(), idCurso.getIdCurso());
                materiacursoporusuario.setIdCurso(idCurso);
            }
            Materia idMateria = materiacursoporusuario.getIdMateria();
            if (idMateria != null) {
                idMateria = em.getReference(idMateria.getClass(), idMateria.getIdMateria());
                materiacursoporusuario.setIdMateria(idMateria);
            }
            em.persist(materiacursoporusuario);
            if (idUsuario != null) {
                idUsuario.getMateriacursoporusuarioList().add(materiacursoporusuario);
                idUsuario = em.merge(idUsuario);
            }
            if (idCurso != null) {
                idCurso.getMateriacursoporusuarioList().add(materiacursoporusuario);
                idCurso = em.merge(idCurso);
            }
            if (idMateria != null) {
                idMateria.getMateriacursoporusuarioList().add(materiacursoporusuario);
                idMateria = em.merge(idMateria);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Materiacursoporusuario materiacursoporusuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materiacursoporusuario persistentMateriacursoporusuario = em.find(Materiacursoporusuario.class, materiacursoporusuario.getIdMateriacursoporusuario());
            Usuario idUsuarioOld = persistentMateriacursoporusuario.getIdUsuario();
            Usuario idUsuarioNew = materiacursoporusuario.getIdUsuario();
            Curso idCursoOld = persistentMateriacursoporusuario.getIdCurso();
            Curso idCursoNew = materiacursoporusuario.getIdCurso();
            Materia idMateriaOld = persistentMateriacursoporusuario.getIdMateria();
            Materia idMateriaNew = materiacursoporusuario.getIdMateria();
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                materiacursoporusuario.setIdUsuario(idUsuarioNew);
            }
            if (idCursoNew != null) {
                idCursoNew = em.getReference(idCursoNew.getClass(), idCursoNew.getIdCurso());
                materiacursoporusuario.setIdCurso(idCursoNew);
            }
            if (idMateriaNew != null) {
                idMateriaNew = em.getReference(idMateriaNew.getClass(), idMateriaNew.getIdMateria());
                materiacursoporusuario.setIdMateria(idMateriaNew);
            }
            materiacursoporusuario = em.merge(materiacursoporusuario);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getMateriacursoporusuarioList().remove(materiacursoporusuario);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getMateriacursoporusuarioList().add(materiacursoporusuario);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            if (idCursoOld != null && !idCursoOld.equals(idCursoNew)) {
                idCursoOld.getMateriacursoporusuarioList().remove(materiacursoporusuario);
                idCursoOld = em.merge(idCursoOld);
            }
            if (idCursoNew != null && !idCursoNew.equals(idCursoOld)) {
                idCursoNew.getMateriacursoporusuarioList().add(materiacursoporusuario);
                idCursoNew = em.merge(idCursoNew);
            }
            if (idMateriaOld != null && !idMateriaOld.equals(idMateriaNew)) {
                idMateriaOld.getMateriacursoporusuarioList().remove(materiacursoporusuario);
                idMateriaOld = em.merge(idMateriaOld);
            }
            if (idMateriaNew != null && !idMateriaNew.equals(idMateriaOld)) {
                idMateriaNew.getMateriacursoporusuarioList().add(materiacursoporusuario);
                idMateriaNew = em.merge(idMateriaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = materiacursoporusuario.getIdMateriacursoporusuario();
                if (findMateriacursoporusuario(id) == null) {
                    throw new NonexistentEntityException("The materiacursoporusuario with id " + id + " no longer exists.");
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
            Materiacursoporusuario materiacursoporusuario;
            try {
                materiacursoporusuario = em.getReference(Materiacursoporusuario.class, id);
                materiacursoporusuario.getIdMateriacursoporusuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The materiacursoporusuario with id " + id + " no longer exists.", enfe);
            }
            Usuario idUsuario = materiacursoporusuario.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getMateriacursoporusuarioList().remove(materiacursoporusuario);
                idUsuario = em.merge(idUsuario);
            }
            Curso idCurso = materiacursoporusuario.getIdCurso();
            if (idCurso != null) {
                idCurso.getMateriacursoporusuarioList().remove(materiacursoporusuario);
                idCurso = em.merge(idCurso);
            }
            Materia idMateria = materiacursoporusuario.getIdMateria();
            if (idMateria != null) {
                idMateria.getMateriacursoporusuarioList().remove(materiacursoporusuario);
                idMateria = em.merge(idMateria);
            }
            em.remove(materiacursoporusuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Materiacursoporusuario> findMateriacursoporusuarioEntities() {
        return findMateriacursoporusuarioEntities(true, -1, -1);
    }

    public List<Materiacursoporusuario> findMateriacursoporusuarioEntities(int maxResults, int firstResult) {
        return findMateriacursoporusuarioEntities(false, maxResults, firstResult);
    }

    private List<Materiacursoporusuario> findMateriacursoporusuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Materiacursoporusuario.class));
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

    public Materiacursoporusuario findMateriacursoporusuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Materiacursoporusuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getMateriacursoporusuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Materiacursoporusuario> rt = cq.from(Materiacursoporusuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
