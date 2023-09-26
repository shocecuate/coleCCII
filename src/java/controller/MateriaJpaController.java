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
import modelo.Materiacursoporusuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Cursoporcarrera;
import modelo.Materia;

/**
 *
 * @author luismorales
 */
public class MateriaJpaController implements Serializable {

    public MateriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Materia materia) {
        if (materia.getMateriacursoporusuarioList() == null) {
            materia.setMateriacursoporusuarioList(new ArrayList<Materiacursoporusuario>());
        }
        if (materia.getCursoporcarreraList() == null) {
            materia.setCursoporcarreraList(new ArrayList<Cursoporcarrera>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Materiacursoporusuario> attachedMateriacursoporusuarioList = new ArrayList<Materiacursoporusuario>();
            for (Materiacursoporusuario materiacursoporusuarioListMateriacursoporusuarioToAttach : materia.getMateriacursoporusuarioList()) {
                materiacursoporusuarioListMateriacursoporusuarioToAttach = em.getReference(materiacursoporusuarioListMateriacursoporusuarioToAttach.getClass(), materiacursoporusuarioListMateriacursoporusuarioToAttach.getIdMateriacursoporusuario());
                attachedMateriacursoporusuarioList.add(materiacursoporusuarioListMateriacursoporusuarioToAttach);
            }
            materia.setMateriacursoporusuarioList(attachedMateriacursoporusuarioList);
            List<Cursoporcarrera> attachedCursoporcarreraList = new ArrayList<Cursoporcarrera>();
            for (Cursoporcarrera cursoporcarreraListCursoporcarreraToAttach : materia.getCursoporcarreraList()) {
                cursoporcarreraListCursoporcarreraToAttach = em.getReference(cursoporcarreraListCursoporcarreraToAttach.getClass(), cursoporcarreraListCursoporcarreraToAttach.getIdCursoPorCarrera());
                attachedCursoporcarreraList.add(cursoporcarreraListCursoporcarreraToAttach);
            }
            materia.setCursoporcarreraList(attachedCursoporcarreraList);
            em.persist(materia);
            for (Materiacursoporusuario materiacursoporusuarioListMateriacursoporusuario : materia.getMateriacursoporusuarioList()) {
                Materia oldIdMateriaOfMateriacursoporusuarioListMateriacursoporusuario = materiacursoporusuarioListMateriacursoporusuario.getIdMateria();
                materiacursoporusuarioListMateriacursoporusuario.setIdMateria(materia);
                materiacursoporusuarioListMateriacursoporusuario = em.merge(materiacursoporusuarioListMateriacursoporusuario);
                if (oldIdMateriaOfMateriacursoporusuarioListMateriacursoporusuario != null) {
                    oldIdMateriaOfMateriacursoporusuarioListMateriacursoporusuario.getMateriacursoporusuarioList().remove(materiacursoporusuarioListMateriacursoporusuario);
                    oldIdMateriaOfMateriacursoporusuarioListMateriacursoporusuario = em.merge(oldIdMateriaOfMateriacursoporusuarioListMateriacursoporusuario);
                }
            }
            for (Cursoporcarrera cursoporcarreraListCursoporcarrera : materia.getCursoporcarreraList()) {
                Materia oldIdMateriaOfCursoporcarreraListCursoporcarrera = cursoporcarreraListCursoporcarrera.getIdMateria();
                cursoporcarreraListCursoporcarrera.setIdMateria(materia);
                cursoporcarreraListCursoporcarrera = em.merge(cursoporcarreraListCursoporcarrera);
                if (oldIdMateriaOfCursoporcarreraListCursoporcarrera != null) {
                    oldIdMateriaOfCursoporcarreraListCursoporcarrera.getCursoporcarreraList().remove(cursoporcarreraListCursoporcarrera);
                    oldIdMateriaOfCursoporcarreraListCursoporcarrera = em.merge(oldIdMateriaOfCursoporcarreraListCursoporcarrera);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Materia materia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia persistentMateria = em.find(Materia.class, materia.getIdMateria());
            List<Materiacursoporusuario> materiacursoporusuarioListOld = persistentMateria.getMateriacursoporusuarioList();
            List<Materiacursoporusuario> materiacursoporusuarioListNew = materia.getMateriacursoporusuarioList();
            List<Cursoporcarrera> cursoporcarreraListOld = persistentMateria.getCursoporcarreraList();
            List<Cursoporcarrera> cursoporcarreraListNew = materia.getCursoporcarreraList();
            List<String> illegalOrphanMessages = null;
            for (Materiacursoporusuario materiacursoporusuarioListOldMateriacursoporusuario : materiacursoporusuarioListOld) {
                if (!materiacursoporusuarioListNew.contains(materiacursoporusuarioListOldMateriacursoporusuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Materiacursoporusuario " + materiacursoporusuarioListOldMateriacursoporusuario + " since its idMateria field is not nullable.");
                }
            }
            for (Cursoporcarrera cursoporcarreraListOldCursoporcarrera : cursoporcarreraListOld) {
                if (!cursoporcarreraListNew.contains(cursoporcarreraListOldCursoporcarrera)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cursoporcarrera " + cursoporcarreraListOldCursoporcarrera + " since its idMateria field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Materiacursoporusuario> attachedMateriacursoporusuarioListNew = new ArrayList<Materiacursoporusuario>();
            for (Materiacursoporusuario materiacursoporusuarioListNewMateriacursoporusuarioToAttach : materiacursoporusuarioListNew) {
                materiacursoporusuarioListNewMateriacursoporusuarioToAttach = em.getReference(materiacursoporusuarioListNewMateriacursoporusuarioToAttach.getClass(), materiacursoporusuarioListNewMateriacursoporusuarioToAttach.getIdMateriacursoporusuario());
                attachedMateriacursoporusuarioListNew.add(materiacursoporusuarioListNewMateriacursoporusuarioToAttach);
            }
            materiacursoporusuarioListNew = attachedMateriacursoporusuarioListNew;
            materia.setMateriacursoporusuarioList(materiacursoporusuarioListNew);
            List<Cursoporcarrera> attachedCursoporcarreraListNew = new ArrayList<Cursoporcarrera>();
            for (Cursoporcarrera cursoporcarreraListNewCursoporcarreraToAttach : cursoporcarreraListNew) {
                cursoporcarreraListNewCursoporcarreraToAttach = em.getReference(cursoporcarreraListNewCursoporcarreraToAttach.getClass(), cursoporcarreraListNewCursoporcarreraToAttach.getIdCursoPorCarrera());
                attachedCursoporcarreraListNew.add(cursoporcarreraListNewCursoporcarreraToAttach);
            }
            cursoporcarreraListNew = attachedCursoporcarreraListNew;
            materia.setCursoporcarreraList(cursoporcarreraListNew);
            materia = em.merge(materia);
            for (Materiacursoporusuario materiacursoporusuarioListNewMateriacursoporusuario : materiacursoporusuarioListNew) {
                if (!materiacursoporusuarioListOld.contains(materiacursoporusuarioListNewMateriacursoporusuario)) {
                    Materia oldIdMateriaOfMateriacursoporusuarioListNewMateriacursoporusuario = materiacursoporusuarioListNewMateriacursoporusuario.getIdMateria();
                    materiacursoporusuarioListNewMateriacursoporusuario.setIdMateria(materia);
                    materiacursoporusuarioListNewMateriacursoporusuario = em.merge(materiacursoporusuarioListNewMateriacursoporusuario);
                    if (oldIdMateriaOfMateriacursoporusuarioListNewMateriacursoporusuario != null && !oldIdMateriaOfMateriacursoporusuarioListNewMateriacursoporusuario.equals(materia)) {
                        oldIdMateriaOfMateriacursoporusuarioListNewMateriacursoporusuario.getMateriacursoporusuarioList().remove(materiacursoporusuarioListNewMateriacursoporusuario);
                        oldIdMateriaOfMateriacursoporusuarioListNewMateriacursoporusuario = em.merge(oldIdMateriaOfMateriacursoporusuarioListNewMateriacursoporusuario);
                    }
                }
            }
            for (Cursoporcarrera cursoporcarreraListNewCursoporcarrera : cursoporcarreraListNew) {
                if (!cursoporcarreraListOld.contains(cursoporcarreraListNewCursoporcarrera)) {
                    Materia oldIdMateriaOfCursoporcarreraListNewCursoporcarrera = cursoporcarreraListNewCursoporcarrera.getIdMateria();
                    cursoporcarreraListNewCursoporcarrera.setIdMateria(materia);
                    cursoporcarreraListNewCursoporcarrera = em.merge(cursoporcarreraListNewCursoporcarrera);
                    if (oldIdMateriaOfCursoporcarreraListNewCursoporcarrera != null && !oldIdMateriaOfCursoporcarreraListNewCursoporcarrera.equals(materia)) {
                        oldIdMateriaOfCursoporcarreraListNewCursoporcarrera.getCursoporcarreraList().remove(cursoporcarreraListNewCursoporcarrera);
                        oldIdMateriaOfCursoporcarreraListNewCursoporcarrera = em.merge(oldIdMateriaOfCursoporcarreraListNewCursoporcarrera);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = materia.getIdMateria();
                if (findMateria(id) == null) {
                    throw new NonexistentEntityException("The materia with id " + id + " no longer exists.");
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
            Materia materia;
            try {
                materia = em.getReference(Materia.class, id);
                materia.getIdMateria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The materia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Materiacursoporusuario> materiacursoporusuarioListOrphanCheck = materia.getMateriacursoporusuarioList();
            for (Materiacursoporusuario materiacursoporusuarioListOrphanCheckMateriacursoporusuario : materiacursoporusuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the Materiacursoporusuario " + materiacursoporusuarioListOrphanCheckMateriacursoporusuario + " in its materiacursoporusuarioList field has a non-nullable idMateria field.");
            }
            List<Cursoporcarrera> cursoporcarreraListOrphanCheck = materia.getCursoporcarreraList();
            for (Cursoporcarrera cursoporcarreraListOrphanCheckCursoporcarrera : cursoporcarreraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the Cursoporcarrera " + cursoporcarreraListOrphanCheckCursoporcarrera + " in its cursoporcarreraList field has a non-nullable idMateria field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(materia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Materia> findMateriaEntities() {
        return findMateriaEntities(true, -1, -1);
    }

    public List<Materia> findMateriaEntities(int maxResults, int firstResult) {
        return findMateriaEntities(false, maxResults, firstResult);
    }

    private List<Materia> findMateriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Materia.class));
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

    public Materia findMateria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Materia.class, id);
        } finally {
            em.close();
        }
    }

    public int getMateriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Materia> rt = cq.from(Materia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
