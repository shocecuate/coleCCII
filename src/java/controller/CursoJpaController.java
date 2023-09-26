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
import modelo.Horario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Curso;
import modelo.Tarea;
import modelo.Materiacursoporusuario;
import modelo.Nota;
import modelo.Examen;
import modelo.Cursoporcarrera;

/**
 *
 * @author luismorales
 */
public class CursoJpaController implements Serializable {

    public CursoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Curso curso) {
        if (curso.getHorarioList() == null) {
            curso.setHorarioList(new ArrayList<Horario>());
        }
        if (curso.getTareaList() == null) {
            curso.setTareaList(new ArrayList<Tarea>());
        }
        if (curso.getMateriacursoporusuarioList() == null) {
            curso.setMateriacursoporusuarioList(new ArrayList<Materiacursoporusuario>());
        }
        if (curso.getNotaList() == null) {
            curso.setNotaList(new ArrayList<Nota>());
        }
        if (curso.getExamenList() == null) {
            curso.setExamenList(new ArrayList<Examen>());
        }
        if (curso.getCursoporcarreraList() == null) {
            curso.setCursoporcarreraList(new ArrayList<Cursoporcarrera>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Horario> attachedHorarioList = new ArrayList<Horario>();
            for (Horario horarioListHorarioToAttach : curso.getHorarioList()) {
                horarioListHorarioToAttach = em.getReference(horarioListHorarioToAttach.getClass(), horarioListHorarioToAttach.getIdHorario());
                attachedHorarioList.add(horarioListHorarioToAttach);
            }
            curso.setHorarioList(attachedHorarioList);
            List<Tarea> attachedTareaList = new ArrayList<Tarea>();
            for (Tarea tareaListTareaToAttach : curso.getTareaList()) {
                tareaListTareaToAttach = em.getReference(tareaListTareaToAttach.getClass(), tareaListTareaToAttach.getIdTarea());
                attachedTareaList.add(tareaListTareaToAttach);
            }
            curso.setTareaList(attachedTareaList);
            List<Materiacursoporusuario> attachedMateriacursoporusuarioList = new ArrayList<Materiacursoporusuario>();
            for (Materiacursoporusuario materiacursoporusuarioListMateriacursoporusuarioToAttach : curso.getMateriacursoporusuarioList()) {
                materiacursoporusuarioListMateriacursoporusuarioToAttach = em.getReference(materiacursoporusuarioListMateriacursoporusuarioToAttach.getClass(), materiacursoporusuarioListMateriacursoporusuarioToAttach.getIdMateriacursoporusuario());
                attachedMateriacursoporusuarioList.add(materiacursoporusuarioListMateriacursoporusuarioToAttach);
            }
            curso.setMateriacursoporusuarioList(attachedMateriacursoporusuarioList);
            List<Nota> attachedNotaList = new ArrayList<Nota>();
            for (Nota notaListNotaToAttach : curso.getNotaList()) {
                notaListNotaToAttach = em.getReference(notaListNotaToAttach.getClass(), notaListNotaToAttach.getIdNota());
                attachedNotaList.add(notaListNotaToAttach);
            }
            curso.setNotaList(attachedNotaList);
            List<Examen> attachedExamenList = new ArrayList<Examen>();
            for (Examen examenListExamenToAttach : curso.getExamenList()) {
                examenListExamenToAttach = em.getReference(examenListExamenToAttach.getClass(), examenListExamenToAttach.getIdExamen());
                attachedExamenList.add(examenListExamenToAttach);
            }
            curso.setExamenList(attachedExamenList);
            List<Cursoporcarrera> attachedCursoporcarreraList = new ArrayList<Cursoporcarrera>();
            for (Cursoporcarrera cursoporcarreraListCursoporcarreraToAttach : curso.getCursoporcarreraList()) {
                cursoporcarreraListCursoporcarreraToAttach = em.getReference(cursoporcarreraListCursoporcarreraToAttach.getClass(), cursoporcarreraListCursoporcarreraToAttach.getIdCursoPorCarrera());
                attachedCursoporcarreraList.add(cursoporcarreraListCursoporcarreraToAttach);
            }
            curso.setCursoporcarreraList(attachedCursoporcarreraList);
            em.persist(curso);
            for (Horario horarioListHorario : curso.getHorarioList()) {
                Curso oldIdCursoOfHorarioListHorario = horarioListHorario.getIdCurso();
                horarioListHorario.setIdCurso(curso);
                horarioListHorario = em.merge(horarioListHorario);
                if (oldIdCursoOfHorarioListHorario != null) {
                    oldIdCursoOfHorarioListHorario.getHorarioList().remove(horarioListHorario);
                    oldIdCursoOfHorarioListHorario = em.merge(oldIdCursoOfHorarioListHorario);
                }
            }
            for (Tarea tareaListTarea : curso.getTareaList()) {
                Curso oldIdCursoOfTareaListTarea = tareaListTarea.getIdCurso();
                tareaListTarea.setIdCurso(curso);
                tareaListTarea = em.merge(tareaListTarea);
                if (oldIdCursoOfTareaListTarea != null) {
                    oldIdCursoOfTareaListTarea.getTareaList().remove(tareaListTarea);
                    oldIdCursoOfTareaListTarea = em.merge(oldIdCursoOfTareaListTarea);
                }
            }
            for (Materiacursoporusuario materiacursoporusuarioListMateriacursoporusuario : curso.getMateriacursoporusuarioList()) {
                Curso oldIdCursoOfMateriacursoporusuarioListMateriacursoporusuario = materiacursoporusuarioListMateriacursoporusuario.getIdCurso();
                materiacursoporusuarioListMateriacursoporusuario.setIdCurso(curso);
                materiacursoporusuarioListMateriacursoporusuario = em.merge(materiacursoporusuarioListMateriacursoporusuario);
                if (oldIdCursoOfMateriacursoporusuarioListMateriacursoporusuario != null) {
                    oldIdCursoOfMateriacursoporusuarioListMateriacursoporusuario.getMateriacursoporusuarioList().remove(materiacursoporusuarioListMateriacursoporusuario);
                    oldIdCursoOfMateriacursoporusuarioListMateriacursoporusuario = em.merge(oldIdCursoOfMateriacursoporusuarioListMateriacursoporusuario);
                }
            }
            for (Nota notaListNota : curso.getNotaList()) {
                Curso oldIdCursoOfNotaListNota = notaListNota.getIdCurso();
                notaListNota.setIdCurso(curso);
                notaListNota = em.merge(notaListNota);
                if (oldIdCursoOfNotaListNota != null) {
                    oldIdCursoOfNotaListNota.getNotaList().remove(notaListNota);
                    oldIdCursoOfNotaListNota = em.merge(oldIdCursoOfNotaListNota);
                }
            }
            for (Examen examenListExamen : curso.getExamenList()) {
                Curso oldIdCursoOfExamenListExamen = examenListExamen.getIdCurso();
                examenListExamen.setIdCurso(curso);
                examenListExamen = em.merge(examenListExamen);
                if (oldIdCursoOfExamenListExamen != null) {
                    oldIdCursoOfExamenListExamen.getExamenList().remove(examenListExamen);
                    oldIdCursoOfExamenListExamen = em.merge(oldIdCursoOfExamenListExamen);
                }
            }
            for (Cursoporcarrera cursoporcarreraListCursoporcarrera : curso.getCursoporcarreraList()) {
                Curso oldIdCursoOfCursoporcarreraListCursoporcarrera = cursoporcarreraListCursoporcarrera.getIdCurso();
                cursoporcarreraListCursoporcarrera.setIdCurso(curso);
                cursoporcarreraListCursoporcarrera = em.merge(cursoporcarreraListCursoporcarrera);
                if (oldIdCursoOfCursoporcarreraListCursoporcarrera != null) {
                    oldIdCursoOfCursoporcarreraListCursoporcarrera.getCursoporcarreraList().remove(cursoporcarreraListCursoporcarrera);
                    oldIdCursoOfCursoporcarreraListCursoporcarrera = em.merge(oldIdCursoOfCursoporcarreraListCursoporcarrera);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Curso curso) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Curso persistentCurso = em.find(Curso.class, curso.getIdCurso());
            List<Horario> horarioListOld = persistentCurso.getHorarioList();
            List<Horario> horarioListNew = curso.getHorarioList();
            List<Tarea> tareaListOld = persistentCurso.getTareaList();
            List<Tarea> tareaListNew = curso.getTareaList();
            List<Materiacursoporusuario> materiacursoporusuarioListOld = persistentCurso.getMateriacursoporusuarioList();
            List<Materiacursoporusuario> materiacursoporusuarioListNew = curso.getMateriacursoporusuarioList();
            List<Nota> notaListOld = persistentCurso.getNotaList();
            List<Nota> notaListNew = curso.getNotaList();
            List<Examen> examenListOld = persistentCurso.getExamenList();
            List<Examen> examenListNew = curso.getExamenList();
            List<Cursoporcarrera> cursoporcarreraListOld = persistentCurso.getCursoporcarreraList();
            List<Cursoporcarrera> cursoporcarreraListNew = curso.getCursoporcarreraList();
            List<String> illegalOrphanMessages = null;
            for (Horario horarioListOldHorario : horarioListOld) {
                if (!horarioListNew.contains(horarioListOldHorario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Horario " + horarioListOldHorario + " since its idCurso field is not nullable.");
                }
            }
            for (Tarea tareaListOldTarea : tareaListOld) {
                if (!tareaListNew.contains(tareaListOldTarea)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarea " + tareaListOldTarea + " since its idCurso field is not nullable.");
                }
            }
            for (Materiacursoporusuario materiacursoporusuarioListOldMateriacursoporusuario : materiacursoporusuarioListOld) {
                if (!materiacursoporusuarioListNew.contains(materiacursoporusuarioListOldMateriacursoporusuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Materiacursoporusuario " + materiacursoporusuarioListOldMateriacursoporusuario + " since its idCurso field is not nullable.");
                }
            }
            for (Nota notaListOldNota : notaListOld) {
                if (!notaListNew.contains(notaListOldNota)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Nota " + notaListOldNota + " since its idCurso field is not nullable.");
                }
            }
            for (Examen examenListOldExamen : examenListOld) {
                if (!examenListNew.contains(examenListOldExamen)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Examen " + examenListOldExamen + " since its idCurso field is not nullable.");
                }
            }
            for (Cursoporcarrera cursoporcarreraListOldCursoporcarrera : cursoporcarreraListOld) {
                if (!cursoporcarreraListNew.contains(cursoporcarreraListOldCursoporcarrera)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cursoporcarrera " + cursoporcarreraListOldCursoporcarrera + " since its idCurso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Horario> attachedHorarioListNew = new ArrayList<Horario>();
            for (Horario horarioListNewHorarioToAttach : horarioListNew) {
                horarioListNewHorarioToAttach = em.getReference(horarioListNewHorarioToAttach.getClass(), horarioListNewHorarioToAttach.getIdHorario());
                attachedHorarioListNew.add(horarioListNewHorarioToAttach);
            }
            horarioListNew = attachedHorarioListNew;
            curso.setHorarioList(horarioListNew);
            List<Tarea> attachedTareaListNew = new ArrayList<Tarea>();
            for (Tarea tareaListNewTareaToAttach : tareaListNew) {
                tareaListNewTareaToAttach = em.getReference(tareaListNewTareaToAttach.getClass(), tareaListNewTareaToAttach.getIdTarea());
                attachedTareaListNew.add(tareaListNewTareaToAttach);
            }
            tareaListNew = attachedTareaListNew;
            curso.setTareaList(tareaListNew);
            List<Materiacursoporusuario> attachedMateriacursoporusuarioListNew = new ArrayList<Materiacursoporusuario>();
            for (Materiacursoporusuario materiacursoporusuarioListNewMateriacursoporusuarioToAttach : materiacursoporusuarioListNew) {
                materiacursoporusuarioListNewMateriacursoporusuarioToAttach = em.getReference(materiacursoporusuarioListNewMateriacursoporusuarioToAttach.getClass(), materiacursoporusuarioListNewMateriacursoporusuarioToAttach.getIdMateriacursoporusuario());
                attachedMateriacursoporusuarioListNew.add(materiacursoporusuarioListNewMateriacursoporusuarioToAttach);
            }
            materiacursoporusuarioListNew = attachedMateriacursoporusuarioListNew;
            curso.setMateriacursoporusuarioList(materiacursoporusuarioListNew);
            List<Nota> attachedNotaListNew = new ArrayList<Nota>();
            for (Nota notaListNewNotaToAttach : notaListNew) {
                notaListNewNotaToAttach = em.getReference(notaListNewNotaToAttach.getClass(), notaListNewNotaToAttach.getIdNota());
                attachedNotaListNew.add(notaListNewNotaToAttach);
            }
            notaListNew = attachedNotaListNew;
            curso.setNotaList(notaListNew);
            List<Examen> attachedExamenListNew = new ArrayList<Examen>();
            for (Examen examenListNewExamenToAttach : examenListNew) {
                examenListNewExamenToAttach = em.getReference(examenListNewExamenToAttach.getClass(), examenListNewExamenToAttach.getIdExamen());
                attachedExamenListNew.add(examenListNewExamenToAttach);
            }
            examenListNew = attachedExamenListNew;
            curso.setExamenList(examenListNew);
            List<Cursoporcarrera> attachedCursoporcarreraListNew = new ArrayList<Cursoporcarrera>();
            for (Cursoporcarrera cursoporcarreraListNewCursoporcarreraToAttach : cursoporcarreraListNew) {
                cursoporcarreraListNewCursoporcarreraToAttach = em.getReference(cursoporcarreraListNewCursoporcarreraToAttach.getClass(), cursoporcarreraListNewCursoporcarreraToAttach.getIdCursoPorCarrera());
                attachedCursoporcarreraListNew.add(cursoporcarreraListNewCursoporcarreraToAttach);
            }
            cursoporcarreraListNew = attachedCursoporcarreraListNew;
            curso.setCursoporcarreraList(cursoporcarreraListNew);
            curso = em.merge(curso);
            for (Horario horarioListNewHorario : horarioListNew) {
                if (!horarioListOld.contains(horarioListNewHorario)) {
                    Curso oldIdCursoOfHorarioListNewHorario = horarioListNewHorario.getIdCurso();
                    horarioListNewHorario.setIdCurso(curso);
                    horarioListNewHorario = em.merge(horarioListNewHorario);
                    if (oldIdCursoOfHorarioListNewHorario != null && !oldIdCursoOfHorarioListNewHorario.equals(curso)) {
                        oldIdCursoOfHorarioListNewHorario.getHorarioList().remove(horarioListNewHorario);
                        oldIdCursoOfHorarioListNewHorario = em.merge(oldIdCursoOfHorarioListNewHorario);
                    }
                }
            }
            for (Tarea tareaListNewTarea : tareaListNew) {
                if (!tareaListOld.contains(tareaListNewTarea)) {
                    Curso oldIdCursoOfTareaListNewTarea = tareaListNewTarea.getIdCurso();
                    tareaListNewTarea.setIdCurso(curso);
                    tareaListNewTarea = em.merge(tareaListNewTarea);
                    if (oldIdCursoOfTareaListNewTarea != null && !oldIdCursoOfTareaListNewTarea.equals(curso)) {
                        oldIdCursoOfTareaListNewTarea.getTareaList().remove(tareaListNewTarea);
                        oldIdCursoOfTareaListNewTarea = em.merge(oldIdCursoOfTareaListNewTarea);
                    }
                }
            }
            for (Materiacursoporusuario materiacursoporusuarioListNewMateriacursoporusuario : materiacursoporusuarioListNew) {
                if (!materiacursoporusuarioListOld.contains(materiacursoporusuarioListNewMateriacursoporusuario)) {
                    Curso oldIdCursoOfMateriacursoporusuarioListNewMateriacursoporusuario = materiacursoporusuarioListNewMateriacursoporusuario.getIdCurso();
                    materiacursoporusuarioListNewMateriacursoporusuario.setIdCurso(curso);
                    materiacursoporusuarioListNewMateriacursoporusuario = em.merge(materiacursoporusuarioListNewMateriacursoporusuario);
                    if (oldIdCursoOfMateriacursoporusuarioListNewMateriacursoporusuario != null && !oldIdCursoOfMateriacursoporusuarioListNewMateriacursoporusuario.equals(curso)) {
                        oldIdCursoOfMateriacursoporusuarioListNewMateriacursoporusuario.getMateriacursoporusuarioList().remove(materiacursoporusuarioListNewMateriacursoporusuario);
                        oldIdCursoOfMateriacursoporusuarioListNewMateriacursoporusuario = em.merge(oldIdCursoOfMateriacursoporusuarioListNewMateriacursoporusuario);
                    }
                }
            }
            for (Nota notaListNewNota : notaListNew) {
                if (!notaListOld.contains(notaListNewNota)) {
                    Curso oldIdCursoOfNotaListNewNota = notaListNewNota.getIdCurso();
                    notaListNewNota.setIdCurso(curso);
                    notaListNewNota = em.merge(notaListNewNota);
                    if (oldIdCursoOfNotaListNewNota != null && !oldIdCursoOfNotaListNewNota.equals(curso)) {
                        oldIdCursoOfNotaListNewNota.getNotaList().remove(notaListNewNota);
                        oldIdCursoOfNotaListNewNota = em.merge(oldIdCursoOfNotaListNewNota);
                    }
                }
            }
            for (Examen examenListNewExamen : examenListNew) {
                if (!examenListOld.contains(examenListNewExamen)) {
                    Curso oldIdCursoOfExamenListNewExamen = examenListNewExamen.getIdCurso();
                    examenListNewExamen.setIdCurso(curso);
                    examenListNewExamen = em.merge(examenListNewExamen);
                    if (oldIdCursoOfExamenListNewExamen != null && !oldIdCursoOfExamenListNewExamen.equals(curso)) {
                        oldIdCursoOfExamenListNewExamen.getExamenList().remove(examenListNewExamen);
                        oldIdCursoOfExamenListNewExamen = em.merge(oldIdCursoOfExamenListNewExamen);
                    }
                }
            }
            for (Cursoporcarrera cursoporcarreraListNewCursoporcarrera : cursoporcarreraListNew) {
                if (!cursoporcarreraListOld.contains(cursoporcarreraListNewCursoporcarrera)) {
                    Curso oldIdCursoOfCursoporcarreraListNewCursoporcarrera = cursoporcarreraListNewCursoporcarrera.getIdCurso();
                    cursoporcarreraListNewCursoporcarrera.setIdCurso(curso);
                    cursoporcarreraListNewCursoporcarrera = em.merge(cursoporcarreraListNewCursoporcarrera);
                    if (oldIdCursoOfCursoporcarreraListNewCursoporcarrera != null && !oldIdCursoOfCursoporcarreraListNewCursoporcarrera.equals(curso)) {
                        oldIdCursoOfCursoporcarreraListNewCursoporcarrera.getCursoporcarreraList().remove(cursoporcarreraListNewCursoporcarrera);
                        oldIdCursoOfCursoporcarreraListNewCursoporcarrera = em.merge(oldIdCursoOfCursoporcarreraListNewCursoporcarrera);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = curso.getIdCurso();
                if (findCurso(id) == null) {
                    throw new NonexistentEntityException("The curso with id " + id + " no longer exists.");
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
            Curso curso;
            try {
                curso = em.getReference(Curso.class, id);
                curso.getIdCurso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The curso with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Horario> horarioListOrphanCheck = curso.getHorarioList();
            for (Horario horarioListOrphanCheckHorario : horarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Curso (" + curso + ") cannot be destroyed since the Horario " + horarioListOrphanCheckHorario + " in its horarioList field has a non-nullable idCurso field.");
            }
            List<Tarea> tareaListOrphanCheck = curso.getTareaList();
            for (Tarea tareaListOrphanCheckTarea : tareaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Curso (" + curso + ") cannot be destroyed since the Tarea " + tareaListOrphanCheckTarea + " in its tareaList field has a non-nullable idCurso field.");
            }
            List<Materiacursoporusuario> materiacursoporusuarioListOrphanCheck = curso.getMateriacursoporusuarioList();
            for (Materiacursoporusuario materiacursoporusuarioListOrphanCheckMateriacursoporusuario : materiacursoporusuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Curso (" + curso + ") cannot be destroyed since the Materiacursoporusuario " + materiacursoporusuarioListOrphanCheckMateriacursoporusuario + " in its materiacursoporusuarioList field has a non-nullable idCurso field.");
            }
            List<Nota> notaListOrphanCheck = curso.getNotaList();
            for (Nota notaListOrphanCheckNota : notaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Curso (" + curso + ") cannot be destroyed since the Nota " + notaListOrphanCheckNota + " in its notaList field has a non-nullable idCurso field.");
            }
            List<Examen> examenListOrphanCheck = curso.getExamenList();
            for (Examen examenListOrphanCheckExamen : examenListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Curso (" + curso + ") cannot be destroyed since the Examen " + examenListOrphanCheckExamen + " in its examenList field has a non-nullable idCurso field.");
            }
            List<Cursoporcarrera> cursoporcarreraListOrphanCheck = curso.getCursoporcarreraList();
            for (Cursoporcarrera cursoporcarreraListOrphanCheckCursoporcarrera : cursoporcarreraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Curso (" + curso + ") cannot be destroyed since the Cursoporcarrera " + cursoporcarreraListOrphanCheckCursoporcarrera + " in its cursoporcarreraList field has a non-nullable idCurso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(curso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Curso> findCursoEntities() {
        return findCursoEntities(true, -1, -1);
    }

    public List<Curso> findCursoEntities(int maxResults, int firstResult) {
        return findCursoEntities(false, maxResults, firstResult);
    }

    private List<Curso> findCursoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Curso.class));
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

    public Curso findCurso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Curso.class, id);
        } finally {
            em.close();
        }
    }

    public int getCursoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Curso> rt = cq.from(Curso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
