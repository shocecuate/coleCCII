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
import modelo.Bimestreporcarrera;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Alumno;
import modelo.Carrera;
import modelo.Cursoporcarrera;

/**
 *
 * @author luismorales
 */
public class CarreraJpaController implements Serializable {

    public CarreraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Carrera carrera) {
        if (carrera.getBimestreporcarreraList() == null) {
            carrera.setBimestreporcarreraList(new ArrayList<Bimestreporcarrera>());
        }
        if (carrera.getAlumnoList() == null) {
            carrera.setAlumnoList(new ArrayList<Alumno>());
        }
        if (carrera.getCursoporcarreraList() == null) {
            carrera.setCursoporcarreraList(new ArrayList<Cursoporcarrera>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Bimestreporcarrera> attachedBimestreporcarreraList = new ArrayList<Bimestreporcarrera>();
            for (Bimestreporcarrera bimestreporcarreraListBimestreporcarreraToAttach : carrera.getBimestreporcarreraList()) {
                bimestreporcarreraListBimestreporcarreraToAttach = em.getReference(bimestreporcarreraListBimestreporcarreraToAttach.getClass(), bimestreporcarreraListBimestreporcarreraToAttach.getIdBimestreporcarrera());
                attachedBimestreporcarreraList.add(bimestreporcarreraListBimestreporcarreraToAttach);
            }
            carrera.setBimestreporcarreraList(attachedBimestreporcarreraList);
            List<Alumno> attachedAlumnoList = new ArrayList<Alumno>();
            for (Alumno alumnoListAlumnoToAttach : carrera.getAlumnoList()) {
                alumnoListAlumnoToAttach = em.getReference(alumnoListAlumnoToAttach.getClass(), alumnoListAlumnoToAttach.getIdAlumno());
                attachedAlumnoList.add(alumnoListAlumnoToAttach);
            }
            carrera.setAlumnoList(attachedAlumnoList);
            List<Cursoporcarrera> attachedCursoporcarreraList = new ArrayList<Cursoporcarrera>();
            for (Cursoporcarrera cursoporcarreraListCursoporcarreraToAttach : carrera.getCursoporcarreraList()) {
                cursoporcarreraListCursoporcarreraToAttach = em.getReference(cursoporcarreraListCursoporcarreraToAttach.getClass(), cursoporcarreraListCursoporcarreraToAttach.getIdCursoPorCarrera());
                attachedCursoporcarreraList.add(cursoporcarreraListCursoporcarreraToAttach);
            }
            carrera.setCursoporcarreraList(attachedCursoporcarreraList);
            em.persist(carrera);
            for (Bimestreporcarrera bimestreporcarreraListBimestreporcarrera : carrera.getBimestreporcarreraList()) {
                Carrera oldIdCarreraOfBimestreporcarreraListBimestreporcarrera = bimestreporcarreraListBimestreporcarrera.getIdCarrera();
                bimestreporcarreraListBimestreporcarrera.setIdCarrera(carrera);
                bimestreporcarreraListBimestreporcarrera = em.merge(bimestreporcarreraListBimestreporcarrera);
                if (oldIdCarreraOfBimestreporcarreraListBimestreporcarrera != null) {
                    oldIdCarreraOfBimestreporcarreraListBimestreporcarrera.getBimestreporcarreraList().remove(bimestreporcarreraListBimestreporcarrera);
                    oldIdCarreraOfBimestreporcarreraListBimestreporcarrera = em.merge(oldIdCarreraOfBimestreporcarreraListBimestreporcarrera);
                }
            }
            for (Alumno alumnoListAlumno : carrera.getAlumnoList()) {
                Carrera oldIdCarreraOfAlumnoListAlumno = alumnoListAlumno.getIdCarrera();
                alumnoListAlumno.setIdCarrera(carrera);
                alumnoListAlumno = em.merge(alumnoListAlumno);
                if (oldIdCarreraOfAlumnoListAlumno != null) {
                    oldIdCarreraOfAlumnoListAlumno.getAlumnoList().remove(alumnoListAlumno);
                    oldIdCarreraOfAlumnoListAlumno = em.merge(oldIdCarreraOfAlumnoListAlumno);
                }
            }
            for (Cursoporcarrera cursoporcarreraListCursoporcarrera : carrera.getCursoporcarreraList()) {
                Carrera oldIdCarreraOfCursoporcarreraListCursoporcarrera = cursoporcarreraListCursoporcarrera.getIdCarrera();
                cursoporcarreraListCursoporcarrera.setIdCarrera(carrera);
                cursoporcarreraListCursoporcarrera = em.merge(cursoporcarreraListCursoporcarrera);
                if (oldIdCarreraOfCursoporcarreraListCursoporcarrera != null) {
                    oldIdCarreraOfCursoporcarreraListCursoporcarrera.getCursoporcarreraList().remove(cursoporcarreraListCursoporcarrera);
                    oldIdCarreraOfCursoporcarreraListCursoporcarrera = em.merge(oldIdCarreraOfCursoporcarreraListCursoporcarrera);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Carrera carrera) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carrera persistentCarrera = em.find(Carrera.class, carrera.getIdCarrera());
            List<Bimestreporcarrera> bimestreporcarreraListOld = persistentCarrera.getBimestreporcarreraList();
            List<Bimestreporcarrera> bimestreporcarreraListNew = carrera.getBimestreporcarreraList();
            List<Alumno> alumnoListOld = persistentCarrera.getAlumnoList();
            List<Alumno> alumnoListNew = carrera.getAlumnoList();
            List<Cursoporcarrera> cursoporcarreraListOld = persistentCarrera.getCursoporcarreraList();
            List<Cursoporcarrera> cursoporcarreraListNew = carrera.getCursoporcarreraList();
            List<String> illegalOrphanMessages = null;
            for (Bimestreporcarrera bimestreporcarreraListOldBimestreporcarrera : bimestreporcarreraListOld) {
                if (!bimestreporcarreraListNew.contains(bimestreporcarreraListOldBimestreporcarrera)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Bimestreporcarrera " + bimestreporcarreraListOldBimestreporcarrera + " since its idCarrera field is not nullable.");
                }
            }
            for (Alumno alumnoListOldAlumno : alumnoListOld) {
                if (!alumnoListNew.contains(alumnoListOldAlumno)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Alumno " + alumnoListOldAlumno + " since its idCarrera field is not nullable.");
                }
            }
            for (Cursoporcarrera cursoporcarreraListOldCursoporcarrera : cursoporcarreraListOld) {
                if (!cursoporcarreraListNew.contains(cursoporcarreraListOldCursoporcarrera)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cursoporcarrera " + cursoporcarreraListOldCursoporcarrera + " since its idCarrera field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Bimestreporcarrera> attachedBimestreporcarreraListNew = new ArrayList<Bimestreporcarrera>();
            for (Bimestreporcarrera bimestreporcarreraListNewBimestreporcarreraToAttach : bimestreporcarreraListNew) {
                bimestreporcarreraListNewBimestreporcarreraToAttach = em.getReference(bimestreporcarreraListNewBimestreporcarreraToAttach.getClass(), bimestreporcarreraListNewBimestreporcarreraToAttach.getIdBimestreporcarrera());
                attachedBimestreporcarreraListNew.add(bimestreporcarreraListNewBimestreporcarreraToAttach);
            }
            bimestreporcarreraListNew = attachedBimestreporcarreraListNew;
            carrera.setBimestreporcarreraList(bimestreporcarreraListNew);
            List<Alumno> attachedAlumnoListNew = new ArrayList<Alumno>();
            for (Alumno alumnoListNewAlumnoToAttach : alumnoListNew) {
                alumnoListNewAlumnoToAttach = em.getReference(alumnoListNewAlumnoToAttach.getClass(), alumnoListNewAlumnoToAttach.getIdAlumno());
                attachedAlumnoListNew.add(alumnoListNewAlumnoToAttach);
            }
            alumnoListNew = attachedAlumnoListNew;
            carrera.setAlumnoList(alumnoListNew);
            List<Cursoporcarrera> attachedCursoporcarreraListNew = new ArrayList<Cursoporcarrera>();
            for (Cursoporcarrera cursoporcarreraListNewCursoporcarreraToAttach : cursoporcarreraListNew) {
                cursoporcarreraListNewCursoporcarreraToAttach = em.getReference(cursoporcarreraListNewCursoporcarreraToAttach.getClass(), cursoporcarreraListNewCursoporcarreraToAttach.getIdCursoPorCarrera());
                attachedCursoporcarreraListNew.add(cursoporcarreraListNewCursoporcarreraToAttach);
            }
            cursoporcarreraListNew = attachedCursoporcarreraListNew;
            carrera.setCursoporcarreraList(cursoporcarreraListNew);
            carrera = em.merge(carrera);
            for (Bimestreporcarrera bimestreporcarreraListNewBimestreporcarrera : bimestreporcarreraListNew) {
                if (!bimestreporcarreraListOld.contains(bimestreporcarreraListNewBimestreporcarrera)) {
                    Carrera oldIdCarreraOfBimestreporcarreraListNewBimestreporcarrera = bimestreporcarreraListNewBimestreporcarrera.getIdCarrera();
                    bimestreporcarreraListNewBimestreporcarrera.setIdCarrera(carrera);
                    bimestreporcarreraListNewBimestreporcarrera = em.merge(bimestreporcarreraListNewBimestreporcarrera);
                    if (oldIdCarreraOfBimestreporcarreraListNewBimestreporcarrera != null && !oldIdCarreraOfBimestreporcarreraListNewBimestreporcarrera.equals(carrera)) {
                        oldIdCarreraOfBimestreporcarreraListNewBimestreporcarrera.getBimestreporcarreraList().remove(bimestreporcarreraListNewBimestreporcarrera);
                        oldIdCarreraOfBimestreporcarreraListNewBimestreporcarrera = em.merge(oldIdCarreraOfBimestreporcarreraListNewBimestreporcarrera);
                    }
                }
            }
            for (Alumno alumnoListNewAlumno : alumnoListNew) {
                if (!alumnoListOld.contains(alumnoListNewAlumno)) {
                    Carrera oldIdCarreraOfAlumnoListNewAlumno = alumnoListNewAlumno.getIdCarrera();
                    alumnoListNewAlumno.setIdCarrera(carrera);
                    alumnoListNewAlumno = em.merge(alumnoListNewAlumno);
                    if (oldIdCarreraOfAlumnoListNewAlumno != null && !oldIdCarreraOfAlumnoListNewAlumno.equals(carrera)) {
                        oldIdCarreraOfAlumnoListNewAlumno.getAlumnoList().remove(alumnoListNewAlumno);
                        oldIdCarreraOfAlumnoListNewAlumno = em.merge(oldIdCarreraOfAlumnoListNewAlumno);
                    }
                }
            }
            for (Cursoporcarrera cursoporcarreraListNewCursoporcarrera : cursoporcarreraListNew) {
                if (!cursoporcarreraListOld.contains(cursoporcarreraListNewCursoporcarrera)) {
                    Carrera oldIdCarreraOfCursoporcarreraListNewCursoporcarrera = cursoporcarreraListNewCursoporcarrera.getIdCarrera();
                    cursoporcarreraListNewCursoporcarrera.setIdCarrera(carrera);
                    cursoporcarreraListNewCursoporcarrera = em.merge(cursoporcarreraListNewCursoporcarrera);
                    if (oldIdCarreraOfCursoporcarreraListNewCursoporcarrera != null && !oldIdCarreraOfCursoporcarreraListNewCursoporcarrera.equals(carrera)) {
                        oldIdCarreraOfCursoporcarreraListNewCursoporcarrera.getCursoporcarreraList().remove(cursoporcarreraListNewCursoporcarrera);
                        oldIdCarreraOfCursoporcarreraListNewCursoporcarrera = em.merge(oldIdCarreraOfCursoporcarreraListNewCursoporcarrera);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = carrera.getIdCarrera();
                if (findCarrera(id) == null) {
                    throw new NonexistentEntityException("The carrera with id " + id + " no longer exists.");
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
            Carrera carrera;
            try {
                carrera = em.getReference(Carrera.class, id);
                carrera.getIdCarrera();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carrera with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Bimestreporcarrera> bimestreporcarreraListOrphanCheck = carrera.getBimestreporcarreraList();
            for (Bimestreporcarrera bimestreporcarreraListOrphanCheckBimestreporcarrera : bimestreporcarreraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Carrera (" + carrera + ") cannot be destroyed since the Bimestreporcarrera " + bimestreporcarreraListOrphanCheckBimestreporcarrera + " in its bimestreporcarreraList field has a non-nullable idCarrera field.");
            }
            List<Alumno> alumnoListOrphanCheck = carrera.getAlumnoList();
            for (Alumno alumnoListOrphanCheckAlumno : alumnoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Carrera (" + carrera + ") cannot be destroyed since the Alumno " + alumnoListOrphanCheckAlumno + " in its alumnoList field has a non-nullable idCarrera field.");
            }
            List<Cursoporcarrera> cursoporcarreraListOrphanCheck = carrera.getCursoporcarreraList();
            for (Cursoporcarrera cursoporcarreraListOrphanCheckCursoporcarrera : cursoporcarreraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Carrera (" + carrera + ") cannot be destroyed since the Cursoporcarrera " + cursoporcarreraListOrphanCheckCursoporcarrera + " in its cursoporcarreraList field has a non-nullable idCarrera field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(carrera);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Carrera> findCarreraEntities() {
        return findCarreraEntities(true, -1, -1);
    }

    public List<Carrera> findCarreraEntities(int maxResults, int firstResult) {
        return findCarreraEntities(false, maxResults, firstResult);
    }

    private List<Carrera> findCarreraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Carrera.class));
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

    public Carrera findCarrera(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Carrera.class, id);
        } finally {
            em.close();
        }
    }

    public int getCarreraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Carrera> rt = cq.from(Carrera.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
