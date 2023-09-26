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
import modelo.Bimestre;
import modelo.Nota;
import modelo.Examen;

/**
 *
 * @author luismorales
 */
public class BimestreJpaController implements Serializable {

    public BimestreJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Bimestre bimestre) {
        if (bimestre.getBimestreporcarreraList() == null) {
            bimestre.setBimestreporcarreraList(new ArrayList<Bimestreporcarrera>());
        }
        if (bimestre.getNotaList() == null) {
            bimestre.setNotaList(new ArrayList<Nota>());
        }
        if (bimestre.getExamenList() == null) {
            bimestre.setExamenList(new ArrayList<Examen>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Bimestreporcarrera> attachedBimestreporcarreraList = new ArrayList<Bimestreporcarrera>();
            for (Bimestreporcarrera bimestreporcarreraListBimestreporcarreraToAttach : bimestre.getBimestreporcarreraList()) {
                bimestreporcarreraListBimestreporcarreraToAttach = em.getReference(bimestreporcarreraListBimestreporcarreraToAttach.getClass(), bimestreporcarreraListBimestreporcarreraToAttach.getIdBimestreporcarrera());
                attachedBimestreporcarreraList.add(bimestreporcarreraListBimestreporcarreraToAttach);
            }
            bimestre.setBimestreporcarreraList(attachedBimestreporcarreraList);
            List<Nota> attachedNotaList = new ArrayList<Nota>();
            for (Nota notaListNotaToAttach : bimestre.getNotaList()) {
                notaListNotaToAttach = em.getReference(notaListNotaToAttach.getClass(), notaListNotaToAttach.getIdNota());
                attachedNotaList.add(notaListNotaToAttach);
            }
            bimestre.setNotaList(attachedNotaList);
            List<Examen> attachedExamenList = new ArrayList<Examen>();
            for (Examen examenListExamenToAttach : bimestre.getExamenList()) {
                examenListExamenToAttach = em.getReference(examenListExamenToAttach.getClass(), examenListExamenToAttach.getIdExamen());
                attachedExamenList.add(examenListExamenToAttach);
            }
            bimestre.setExamenList(attachedExamenList);
            em.persist(bimestre);
            for (Bimestreporcarrera bimestreporcarreraListBimestreporcarrera : bimestre.getBimestreporcarreraList()) {
                Bimestre oldIdBimestreOfBimestreporcarreraListBimestreporcarrera = bimestreporcarreraListBimestreporcarrera.getIdBimestre();
                bimestreporcarreraListBimestreporcarrera.setIdBimestre(bimestre);
                bimestreporcarreraListBimestreporcarrera = em.merge(bimestreporcarreraListBimestreporcarrera);
                if (oldIdBimestreOfBimestreporcarreraListBimestreporcarrera != null) {
                    oldIdBimestreOfBimestreporcarreraListBimestreporcarrera.getBimestreporcarreraList().remove(bimestreporcarreraListBimestreporcarrera);
                    oldIdBimestreOfBimestreporcarreraListBimestreporcarrera = em.merge(oldIdBimestreOfBimestreporcarreraListBimestreporcarrera);
                }
            }
            for (Nota notaListNota : bimestre.getNotaList()) {
                Bimestre oldIdBimestreOfNotaListNota = notaListNota.getIdBimestre();
                notaListNota.setIdBimestre(bimestre);
                notaListNota = em.merge(notaListNota);
                if (oldIdBimestreOfNotaListNota != null) {
                    oldIdBimestreOfNotaListNota.getNotaList().remove(notaListNota);
                    oldIdBimestreOfNotaListNota = em.merge(oldIdBimestreOfNotaListNota);
                }
            }
            for (Examen examenListExamen : bimestre.getExamenList()) {
                Bimestre oldIdBimestreOfExamenListExamen = examenListExamen.getIdBimestre();
                examenListExamen.setIdBimestre(bimestre);
                examenListExamen = em.merge(examenListExamen);
                if (oldIdBimestreOfExamenListExamen != null) {
                    oldIdBimestreOfExamenListExamen.getExamenList().remove(examenListExamen);
                    oldIdBimestreOfExamenListExamen = em.merge(oldIdBimestreOfExamenListExamen);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Bimestre bimestre) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Bimestre persistentBimestre = em.find(Bimestre.class, bimestre.getIdBimestre());
            List<Bimestreporcarrera> bimestreporcarreraListOld = persistentBimestre.getBimestreporcarreraList();
            List<Bimestreporcarrera> bimestreporcarreraListNew = bimestre.getBimestreporcarreraList();
            List<Nota> notaListOld = persistentBimestre.getNotaList();
            List<Nota> notaListNew = bimestre.getNotaList();
            List<Examen> examenListOld = persistentBimestre.getExamenList();
            List<Examen> examenListNew = bimestre.getExamenList();
            List<String> illegalOrphanMessages = null;
            for (Bimestreporcarrera bimestreporcarreraListOldBimestreporcarrera : bimestreporcarreraListOld) {
                if (!bimestreporcarreraListNew.contains(bimestreporcarreraListOldBimestreporcarrera)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Bimestreporcarrera " + bimestreporcarreraListOldBimestreporcarrera + " since its idBimestre field is not nullable.");
                }
            }
            for (Nota notaListOldNota : notaListOld) {
                if (!notaListNew.contains(notaListOldNota)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Nota " + notaListOldNota + " since its idBimestre field is not nullable.");
                }
            }
            for (Examen examenListOldExamen : examenListOld) {
                if (!examenListNew.contains(examenListOldExamen)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Examen " + examenListOldExamen + " since its idBimestre field is not nullable.");
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
            bimestre.setBimestreporcarreraList(bimestreporcarreraListNew);
            List<Nota> attachedNotaListNew = new ArrayList<Nota>();
            for (Nota notaListNewNotaToAttach : notaListNew) {
                notaListNewNotaToAttach = em.getReference(notaListNewNotaToAttach.getClass(), notaListNewNotaToAttach.getIdNota());
                attachedNotaListNew.add(notaListNewNotaToAttach);
            }
            notaListNew = attachedNotaListNew;
            bimestre.setNotaList(notaListNew);
            List<Examen> attachedExamenListNew = new ArrayList<Examen>();
            for (Examen examenListNewExamenToAttach : examenListNew) {
                examenListNewExamenToAttach = em.getReference(examenListNewExamenToAttach.getClass(), examenListNewExamenToAttach.getIdExamen());
                attachedExamenListNew.add(examenListNewExamenToAttach);
            }
            examenListNew = attachedExamenListNew;
            bimestre.setExamenList(examenListNew);
            bimestre = em.merge(bimestre);
            for (Bimestreporcarrera bimestreporcarreraListNewBimestreporcarrera : bimestreporcarreraListNew) {
                if (!bimestreporcarreraListOld.contains(bimestreporcarreraListNewBimestreporcarrera)) {
                    Bimestre oldIdBimestreOfBimestreporcarreraListNewBimestreporcarrera = bimestreporcarreraListNewBimestreporcarrera.getIdBimestre();
                    bimestreporcarreraListNewBimestreporcarrera.setIdBimestre(bimestre);
                    bimestreporcarreraListNewBimestreporcarrera = em.merge(bimestreporcarreraListNewBimestreporcarrera);
                    if (oldIdBimestreOfBimestreporcarreraListNewBimestreporcarrera != null && !oldIdBimestreOfBimestreporcarreraListNewBimestreporcarrera.equals(bimestre)) {
                        oldIdBimestreOfBimestreporcarreraListNewBimestreporcarrera.getBimestreporcarreraList().remove(bimestreporcarreraListNewBimestreporcarrera);
                        oldIdBimestreOfBimestreporcarreraListNewBimestreporcarrera = em.merge(oldIdBimestreOfBimestreporcarreraListNewBimestreporcarrera);
                    }
                }
            }
            for (Nota notaListNewNota : notaListNew) {
                if (!notaListOld.contains(notaListNewNota)) {
                    Bimestre oldIdBimestreOfNotaListNewNota = notaListNewNota.getIdBimestre();
                    notaListNewNota.setIdBimestre(bimestre);
                    notaListNewNota = em.merge(notaListNewNota);
                    if (oldIdBimestreOfNotaListNewNota != null && !oldIdBimestreOfNotaListNewNota.equals(bimestre)) {
                        oldIdBimestreOfNotaListNewNota.getNotaList().remove(notaListNewNota);
                        oldIdBimestreOfNotaListNewNota = em.merge(oldIdBimestreOfNotaListNewNota);
                    }
                }
            }
            for (Examen examenListNewExamen : examenListNew) {
                if (!examenListOld.contains(examenListNewExamen)) {
                    Bimestre oldIdBimestreOfExamenListNewExamen = examenListNewExamen.getIdBimestre();
                    examenListNewExamen.setIdBimestre(bimestre);
                    examenListNewExamen = em.merge(examenListNewExamen);
                    if (oldIdBimestreOfExamenListNewExamen != null && !oldIdBimestreOfExamenListNewExamen.equals(bimestre)) {
                        oldIdBimestreOfExamenListNewExamen.getExamenList().remove(examenListNewExamen);
                        oldIdBimestreOfExamenListNewExamen = em.merge(oldIdBimestreOfExamenListNewExamen);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = bimestre.getIdBimestre();
                if (findBimestre(id) == null) {
                    throw new NonexistentEntityException("The bimestre with id " + id + " no longer exists.");
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
            Bimestre bimestre;
            try {
                bimestre = em.getReference(Bimestre.class, id);
                bimestre.getIdBimestre();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bimestre with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Bimestreporcarrera> bimestreporcarreraListOrphanCheck = bimestre.getBimestreporcarreraList();
            for (Bimestreporcarrera bimestreporcarreraListOrphanCheckBimestreporcarrera : bimestreporcarreraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Bimestre (" + bimestre + ") cannot be destroyed since the Bimestreporcarrera " + bimestreporcarreraListOrphanCheckBimestreporcarrera + " in its bimestreporcarreraList field has a non-nullable idBimestre field.");
            }
            List<Nota> notaListOrphanCheck = bimestre.getNotaList();
            for (Nota notaListOrphanCheckNota : notaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Bimestre (" + bimestre + ") cannot be destroyed since the Nota " + notaListOrphanCheckNota + " in its notaList field has a non-nullable idBimestre field.");
            }
            List<Examen> examenListOrphanCheck = bimestre.getExamenList();
            for (Examen examenListOrphanCheckExamen : examenListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Bimestre (" + bimestre + ") cannot be destroyed since the Examen " + examenListOrphanCheckExamen + " in its examenList field has a non-nullable idBimestre field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(bimestre);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Bimestre> findBimestreEntities() {
        return findBimestreEntities(true, -1, -1);
    }

    public List<Bimestre> findBimestreEntities(int maxResults, int firstResult) {
        return findBimestreEntities(false, maxResults, firstResult);
    }

    private List<Bimestre> findBimestreEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Bimestre.class));
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

    public Bimestre findBimestre(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Bimestre.class, id);
        } finally {
            em.close();
        }
    }

    public int getBimestreCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Bimestre> rt = cq.from(Bimestre.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
