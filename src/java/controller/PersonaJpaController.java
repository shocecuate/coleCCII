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
import modelo.TelefonoPersona;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Documentosporpersona;
import modelo.Direccion;
import modelo.Alumno;
import modelo.Profesor;
import modelo.Inscripcion;
import modelo.Usuario;
import modelo.Mensaje;
import modelo.Persona;

/**
 *
 * @author luismorales
 */
public class PersonaJpaController implements Serializable {

    public PersonaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Persona persona) {
        if (persona.getTelefonoPersonaList() == null) {
            persona.setTelefonoPersonaList(new ArrayList<TelefonoPersona>());
        }
        if (persona.getDocumentosporpersonaList() == null) {
            persona.setDocumentosporpersonaList(new ArrayList<Documentosporpersona>());
        }
        if (persona.getDireccionList() == null) {
            persona.setDireccionList(new ArrayList<Direccion>());
        }
        if (persona.getAlumnoList() == null) {
            persona.setAlumnoList(new ArrayList<Alumno>());
        }
        if (persona.getProfesorList() == null) {
            persona.setProfesorList(new ArrayList<Profesor>());
        }
        if (persona.getInscripcionList() == null) {
            persona.setInscripcionList(new ArrayList<Inscripcion>());
        }
        if (persona.getUsuarioList() == null) {
            persona.setUsuarioList(new ArrayList<Usuario>());
        }
        if (persona.getMensajeList() == null) {
            persona.setMensajeList(new ArrayList<Mensaje>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TelefonoPersona> attachedTelefonoPersonaList = new ArrayList<TelefonoPersona>();
            for (TelefonoPersona telefonoPersonaListTelefonoPersonaToAttach : persona.getTelefonoPersonaList()) {
                telefonoPersonaListTelefonoPersonaToAttach = em.getReference(telefonoPersonaListTelefonoPersonaToAttach.getClass(), telefonoPersonaListTelefonoPersonaToAttach.getIdTelefonoPersona());
                attachedTelefonoPersonaList.add(telefonoPersonaListTelefonoPersonaToAttach);
            }
            persona.setTelefonoPersonaList(attachedTelefonoPersonaList);
            List<Documentosporpersona> attachedDocumentosporpersonaList = new ArrayList<Documentosporpersona>();
            for (Documentosporpersona documentosporpersonaListDocumentosporpersonaToAttach : persona.getDocumentosporpersonaList()) {
                documentosporpersonaListDocumentosporpersonaToAttach = em.getReference(documentosporpersonaListDocumentosporpersonaToAttach.getClass(), documentosporpersonaListDocumentosporpersonaToAttach.getIdDocPorPersona());
                attachedDocumentosporpersonaList.add(documentosporpersonaListDocumentosporpersonaToAttach);
            }
            persona.setDocumentosporpersonaList(attachedDocumentosporpersonaList);
            List<Direccion> attachedDireccionList = new ArrayList<Direccion>();
            for (Direccion direccionListDireccionToAttach : persona.getDireccionList()) {
                direccionListDireccionToAttach = em.getReference(direccionListDireccionToAttach.getClass(), direccionListDireccionToAttach.getIdDireccion());
                attachedDireccionList.add(direccionListDireccionToAttach);
            }
            persona.setDireccionList(attachedDireccionList);
            List<Alumno> attachedAlumnoList = new ArrayList<Alumno>();
            for (Alumno alumnoListAlumnoToAttach : persona.getAlumnoList()) {
                alumnoListAlumnoToAttach = em.getReference(alumnoListAlumnoToAttach.getClass(), alumnoListAlumnoToAttach.getIdAlumno());
                attachedAlumnoList.add(alumnoListAlumnoToAttach);
            }
            persona.setAlumnoList(attachedAlumnoList);
            List<Profesor> attachedProfesorList = new ArrayList<Profesor>();
            for (Profesor profesorListProfesorToAttach : persona.getProfesorList()) {
                profesorListProfesorToAttach = em.getReference(profesorListProfesorToAttach.getClass(), profesorListProfesorToAttach.getIdProfesor());
                attachedProfesorList.add(profesorListProfesorToAttach);
            }
            persona.setProfesorList(attachedProfesorList);
            List<Inscripcion> attachedInscripcionList = new ArrayList<Inscripcion>();
            for (Inscripcion inscripcionListInscripcionToAttach : persona.getInscripcionList()) {
                inscripcionListInscripcionToAttach = em.getReference(inscripcionListInscripcionToAttach.getClass(), inscripcionListInscripcionToAttach.getIdInscripcion());
                attachedInscripcionList.add(inscripcionListInscripcionToAttach);
            }
            persona.setInscripcionList(attachedInscripcionList);
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : persona.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getIdUsuario());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            persona.setUsuarioList(attachedUsuarioList);
            List<Mensaje> attachedMensajeList = new ArrayList<Mensaje>();
            for (Mensaje mensajeListMensajeToAttach : persona.getMensajeList()) {
                mensajeListMensajeToAttach = em.getReference(mensajeListMensajeToAttach.getClass(), mensajeListMensajeToAttach.getIdMensaje());
                attachedMensajeList.add(mensajeListMensajeToAttach);
            }
            persona.setMensajeList(attachedMensajeList);
            em.persist(persona);
            for (TelefonoPersona telefonoPersonaListTelefonoPersona : persona.getTelefonoPersonaList()) {
                Persona oldIdPersonaOfTelefonoPersonaListTelefonoPersona = telefonoPersonaListTelefonoPersona.getIdPersona();
                telefonoPersonaListTelefonoPersona.setIdPersona(persona);
                telefonoPersonaListTelefonoPersona = em.merge(telefonoPersonaListTelefonoPersona);
                if (oldIdPersonaOfTelefonoPersonaListTelefonoPersona != null) {
                    oldIdPersonaOfTelefonoPersonaListTelefonoPersona.getTelefonoPersonaList().remove(telefonoPersonaListTelefonoPersona);
                    oldIdPersonaOfTelefonoPersonaListTelefonoPersona = em.merge(oldIdPersonaOfTelefonoPersonaListTelefonoPersona);
                }
            }
            for (Documentosporpersona documentosporpersonaListDocumentosporpersona : persona.getDocumentosporpersonaList()) {
                Persona oldIdPersonaOfDocumentosporpersonaListDocumentosporpersona = documentosporpersonaListDocumentosporpersona.getIdPersona();
                documentosporpersonaListDocumentosporpersona.setIdPersona(persona);
                documentosporpersonaListDocumentosporpersona = em.merge(documentosporpersonaListDocumentosporpersona);
                if (oldIdPersonaOfDocumentosporpersonaListDocumentosporpersona != null) {
                    oldIdPersonaOfDocumentosporpersonaListDocumentosporpersona.getDocumentosporpersonaList().remove(documentosporpersonaListDocumentosporpersona);
                    oldIdPersonaOfDocumentosporpersonaListDocumentosporpersona = em.merge(oldIdPersonaOfDocumentosporpersonaListDocumentosporpersona);
                }
            }
            for (Direccion direccionListDireccion : persona.getDireccionList()) {
                Persona oldIdPersonaOfDireccionListDireccion = direccionListDireccion.getIdPersona();
                direccionListDireccion.setIdPersona(persona);
                direccionListDireccion = em.merge(direccionListDireccion);
                if (oldIdPersonaOfDireccionListDireccion != null) {
                    oldIdPersonaOfDireccionListDireccion.getDireccionList().remove(direccionListDireccion);
                    oldIdPersonaOfDireccionListDireccion = em.merge(oldIdPersonaOfDireccionListDireccion);
                }
            }
            for (Alumno alumnoListAlumno : persona.getAlumnoList()) {
                Persona oldIdPersonaOfAlumnoListAlumno = alumnoListAlumno.getIdPersona();
                alumnoListAlumno.setIdPersona(persona);
                alumnoListAlumno = em.merge(alumnoListAlumno);
                if (oldIdPersonaOfAlumnoListAlumno != null) {
                    oldIdPersonaOfAlumnoListAlumno.getAlumnoList().remove(alumnoListAlumno);
                    oldIdPersonaOfAlumnoListAlumno = em.merge(oldIdPersonaOfAlumnoListAlumno);
                }
            }
            for (Profesor profesorListProfesor : persona.getProfesorList()) {
                Persona oldIdPersonaOfProfesorListProfesor = profesorListProfesor.getIdPersona();
                profesorListProfesor.setIdPersona(persona);
                profesorListProfesor = em.merge(profesorListProfesor);
                if (oldIdPersonaOfProfesorListProfesor != null) {
                    oldIdPersonaOfProfesorListProfesor.getProfesorList().remove(profesorListProfesor);
                    oldIdPersonaOfProfesorListProfesor = em.merge(oldIdPersonaOfProfesorListProfesor);
                }
            }
            for (Inscripcion inscripcionListInscripcion : persona.getInscripcionList()) {
                Persona oldIdPersonaOfInscripcionListInscripcion = inscripcionListInscripcion.getIdPersona();
                inscripcionListInscripcion.setIdPersona(persona);
                inscripcionListInscripcion = em.merge(inscripcionListInscripcion);
                if (oldIdPersonaOfInscripcionListInscripcion != null) {
                    oldIdPersonaOfInscripcionListInscripcion.getInscripcionList().remove(inscripcionListInscripcion);
                    oldIdPersonaOfInscripcionListInscripcion = em.merge(oldIdPersonaOfInscripcionListInscripcion);
                }
            }
            for (Usuario usuarioListUsuario : persona.getUsuarioList()) {
                Persona oldIdPersonaOfUsuarioListUsuario = usuarioListUsuario.getIdPersona();
                usuarioListUsuario.setIdPersona(persona);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldIdPersonaOfUsuarioListUsuario != null) {
                    oldIdPersonaOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldIdPersonaOfUsuarioListUsuario = em.merge(oldIdPersonaOfUsuarioListUsuario);
                }
            }
            for (Mensaje mensajeListMensaje : persona.getMensajeList()) {
                Persona oldIdDestinatarioOfMensajeListMensaje = mensajeListMensaje.getIdDestinatario();
                mensajeListMensaje.setIdDestinatario(persona);
                mensajeListMensaje = em.merge(mensajeListMensaje);
                if (oldIdDestinatarioOfMensajeListMensaje != null) {
                    oldIdDestinatarioOfMensajeListMensaje.getMensajeList().remove(mensajeListMensaje);
                    oldIdDestinatarioOfMensajeListMensaje = em.merge(oldIdDestinatarioOfMensajeListMensaje);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Persona persona) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persistentPersona = em.find(Persona.class, persona.getIdPersona());
            List<TelefonoPersona> telefonoPersonaListOld = persistentPersona.getTelefonoPersonaList();
            List<TelefonoPersona> telefonoPersonaListNew = persona.getTelefonoPersonaList();
            List<Documentosporpersona> documentosporpersonaListOld = persistentPersona.getDocumentosporpersonaList();
            List<Documentosporpersona> documentosporpersonaListNew = persona.getDocumentosporpersonaList();
            List<Direccion> direccionListOld = persistentPersona.getDireccionList();
            List<Direccion> direccionListNew = persona.getDireccionList();
            List<Alumno> alumnoListOld = persistentPersona.getAlumnoList();
            List<Alumno> alumnoListNew = persona.getAlumnoList();
            List<Profesor> profesorListOld = persistentPersona.getProfesorList();
            List<Profesor> profesorListNew = persona.getProfesorList();
            List<Inscripcion> inscripcionListOld = persistentPersona.getInscripcionList();
            List<Inscripcion> inscripcionListNew = persona.getInscripcionList();
            List<Usuario> usuarioListOld = persistentPersona.getUsuarioList();
            List<Usuario> usuarioListNew = persona.getUsuarioList();
            List<Mensaje> mensajeListOld = persistentPersona.getMensajeList();
            List<Mensaje> mensajeListNew = persona.getMensajeList();
            List<String> illegalOrphanMessages = null;
            for (TelefonoPersona telefonoPersonaListOldTelefonoPersona : telefonoPersonaListOld) {
                if (!telefonoPersonaListNew.contains(telefonoPersonaListOldTelefonoPersona)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TelefonoPersona " + telefonoPersonaListOldTelefonoPersona + " since its idPersona field is not nullable.");
                }
            }
            for (Documentosporpersona documentosporpersonaListOldDocumentosporpersona : documentosporpersonaListOld) {
                if (!documentosporpersonaListNew.contains(documentosporpersonaListOldDocumentosporpersona)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Documentosporpersona " + documentosporpersonaListOldDocumentosporpersona + " since its idPersona field is not nullable.");
                }
            }
            for (Direccion direccionListOldDireccion : direccionListOld) {
                if (!direccionListNew.contains(direccionListOldDireccion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Direccion " + direccionListOldDireccion + " since its idPersona field is not nullable.");
                }
            }
            for (Alumno alumnoListOldAlumno : alumnoListOld) {
                if (!alumnoListNew.contains(alumnoListOldAlumno)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Alumno " + alumnoListOldAlumno + " since its idPersona field is not nullable.");
                }
            }
            for (Profesor profesorListOldProfesor : profesorListOld) {
                if (!profesorListNew.contains(profesorListOldProfesor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Profesor " + profesorListOldProfesor + " since its idPersona field is not nullable.");
                }
            }
            for (Inscripcion inscripcionListOldInscripcion : inscripcionListOld) {
                if (!inscripcionListNew.contains(inscripcionListOldInscripcion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Inscripcion " + inscripcionListOldInscripcion + " since its idPersona field is not nullable.");
                }
            }
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuario " + usuarioListOldUsuario + " since its idPersona field is not nullable.");
                }
            }
            for (Mensaje mensajeListOldMensaje : mensajeListOld) {
                if (!mensajeListNew.contains(mensajeListOldMensaje)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Mensaje " + mensajeListOldMensaje + " since its idDestinatario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<TelefonoPersona> attachedTelefonoPersonaListNew = new ArrayList<TelefonoPersona>();
            for (TelefonoPersona telefonoPersonaListNewTelefonoPersonaToAttach : telefonoPersonaListNew) {
                telefonoPersonaListNewTelefonoPersonaToAttach = em.getReference(telefonoPersonaListNewTelefonoPersonaToAttach.getClass(), telefonoPersonaListNewTelefonoPersonaToAttach.getIdTelefonoPersona());
                attachedTelefonoPersonaListNew.add(telefonoPersonaListNewTelefonoPersonaToAttach);
            }
            telefonoPersonaListNew = attachedTelefonoPersonaListNew;
            persona.setTelefonoPersonaList(telefonoPersonaListNew);
            List<Documentosporpersona> attachedDocumentosporpersonaListNew = new ArrayList<Documentosporpersona>();
            for (Documentosporpersona documentosporpersonaListNewDocumentosporpersonaToAttach : documentosporpersonaListNew) {
                documentosporpersonaListNewDocumentosporpersonaToAttach = em.getReference(documentosporpersonaListNewDocumentosporpersonaToAttach.getClass(), documentosporpersonaListNewDocumentosporpersonaToAttach.getIdDocPorPersona());
                attachedDocumentosporpersonaListNew.add(documentosporpersonaListNewDocumentosporpersonaToAttach);
            }
            documentosporpersonaListNew = attachedDocumentosporpersonaListNew;
            persona.setDocumentosporpersonaList(documentosporpersonaListNew);
            List<Direccion> attachedDireccionListNew = new ArrayList<Direccion>();
            for (Direccion direccionListNewDireccionToAttach : direccionListNew) {
                direccionListNewDireccionToAttach = em.getReference(direccionListNewDireccionToAttach.getClass(), direccionListNewDireccionToAttach.getIdDireccion());
                attachedDireccionListNew.add(direccionListNewDireccionToAttach);
            }
            direccionListNew = attachedDireccionListNew;
            persona.setDireccionList(direccionListNew);
            List<Alumno> attachedAlumnoListNew = new ArrayList<Alumno>();
            for (Alumno alumnoListNewAlumnoToAttach : alumnoListNew) {
                alumnoListNewAlumnoToAttach = em.getReference(alumnoListNewAlumnoToAttach.getClass(), alumnoListNewAlumnoToAttach.getIdAlumno());
                attachedAlumnoListNew.add(alumnoListNewAlumnoToAttach);
            }
            alumnoListNew = attachedAlumnoListNew;
            persona.setAlumnoList(alumnoListNew);
            List<Profesor> attachedProfesorListNew = new ArrayList<Profesor>();
            for (Profesor profesorListNewProfesorToAttach : profesorListNew) {
                profesorListNewProfesorToAttach = em.getReference(profesorListNewProfesorToAttach.getClass(), profesorListNewProfesorToAttach.getIdProfesor());
                attachedProfesorListNew.add(profesorListNewProfesorToAttach);
            }
            profesorListNew = attachedProfesorListNew;
            persona.setProfesorList(profesorListNew);
            List<Inscripcion> attachedInscripcionListNew = new ArrayList<Inscripcion>();
            for (Inscripcion inscripcionListNewInscripcionToAttach : inscripcionListNew) {
                inscripcionListNewInscripcionToAttach = em.getReference(inscripcionListNewInscripcionToAttach.getClass(), inscripcionListNewInscripcionToAttach.getIdInscripcion());
                attachedInscripcionListNew.add(inscripcionListNewInscripcionToAttach);
            }
            inscripcionListNew = attachedInscripcionListNew;
            persona.setInscripcionList(inscripcionListNew);
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getIdUsuario());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            persona.setUsuarioList(usuarioListNew);
            List<Mensaje> attachedMensajeListNew = new ArrayList<Mensaje>();
            for (Mensaje mensajeListNewMensajeToAttach : mensajeListNew) {
                mensajeListNewMensajeToAttach = em.getReference(mensajeListNewMensajeToAttach.getClass(), mensajeListNewMensajeToAttach.getIdMensaje());
                attachedMensajeListNew.add(mensajeListNewMensajeToAttach);
            }
            mensajeListNew = attachedMensajeListNew;
            persona.setMensajeList(mensajeListNew);
            persona = em.merge(persona);
            for (TelefonoPersona telefonoPersonaListNewTelefonoPersona : telefonoPersonaListNew) {
                if (!telefonoPersonaListOld.contains(telefonoPersonaListNewTelefonoPersona)) {
                    Persona oldIdPersonaOfTelefonoPersonaListNewTelefonoPersona = telefonoPersonaListNewTelefonoPersona.getIdPersona();
                    telefonoPersonaListNewTelefonoPersona.setIdPersona(persona);
                    telefonoPersonaListNewTelefonoPersona = em.merge(telefonoPersonaListNewTelefonoPersona);
                    if (oldIdPersonaOfTelefonoPersonaListNewTelefonoPersona != null && !oldIdPersonaOfTelefonoPersonaListNewTelefonoPersona.equals(persona)) {
                        oldIdPersonaOfTelefonoPersonaListNewTelefonoPersona.getTelefonoPersonaList().remove(telefonoPersonaListNewTelefonoPersona);
                        oldIdPersonaOfTelefonoPersonaListNewTelefonoPersona = em.merge(oldIdPersonaOfTelefonoPersonaListNewTelefonoPersona);
                    }
                }
            }
            for (Documentosporpersona documentosporpersonaListNewDocumentosporpersona : documentosporpersonaListNew) {
                if (!documentosporpersonaListOld.contains(documentosporpersonaListNewDocumentosporpersona)) {
                    Persona oldIdPersonaOfDocumentosporpersonaListNewDocumentosporpersona = documentosporpersonaListNewDocumentosporpersona.getIdPersona();
                    documentosporpersonaListNewDocumentosporpersona.setIdPersona(persona);
                    documentosporpersonaListNewDocumentosporpersona = em.merge(documentosporpersonaListNewDocumentosporpersona);
                    if (oldIdPersonaOfDocumentosporpersonaListNewDocumentosporpersona != null && !oldIdPersonaOfDocumentosporpersonaListNewDocumentosporpersona.equals(persona)) {
                        oldIdPersonaOfDocumentosporpersonaListNewDocumentosporpersona.getDocumentosporpersonaList().remove(documentosporpersonaListNewDocumentosporpersona);
                        oldIdPersonaOfDocumentosporpersonaListNewDocumentosporpersona = em.merge(oldIdPersonaOfDocumentosporpersonaListNewDocumentosporpersona);
                    }
                }
            }
            for (Direccion direccionListNewDireccion : direccionListNew) {
                if (!direccionListOld.contains(direccionListNewDireccion)) {
                    Persona oldIdPersonaOfDireccionListNewDireccion = direccionListNewDireccion.getIdPersona();
                    direccionListNewDireccion.setIdPersona(persona);
                    direccionListNewDireccion = em.merge(direccionListNewDireccion);
                    if (oldIdPersonaOfDireccionListNewDireccion != null && !oldIdPersonaOfDireccionListNewDireccion.equals(persona)) {
                        oldIdPersonaOfDireccionListNewDireccion.getDireccionList().remove(direccionListNewDireccion);
                        oldIdPersonaOfDireccionListNewDireccion = em.merge(oldIdPersonaOfDireccionListNewDireccion);
                    }
                }
            }
            for (Alumno alumnoListNewAlumno : alumnoListNew) {
                if (!alumnoListOld.contains(alumnoListNewAlumno)) {
                    Persona oldIdPersonaOfAlumnoListNewAlumno = alumnoListNewAlumno.getIdPersona();
                    alumnoListNewAlumno.setIdPersona(persona);
                    alumnoListNewAlumno = em.merge(alumnoListNewAlumno);
                    if (oldIdPersonaOfAlumnoListNewAlumno != null && !oldIdPersonaOfAlumnoListNewAlumno.equals(persona)) {
                        oldIdPersonaOfAlumnoListNewAlumno.getAlumnoList().remove(alumnoListNewAlumno);
                        oldIdPersonaOfAlumnoListNewAlumno = em.merge(oldIdPersonaOfAlumnoListNewAlumno);
                    }
                }
            }
            for (Profesor profesorListNewProfesor : profesorListNew) {
                if (!profesorListOld.contains(profesorListNewProfesor)) {
                    Persona oldIdPersonaOfProfesorListNewProfesor = profesorListNewProfesor.getIdPersona();
                    profesorListNewProfesor.setIdPersona(persona);
                    profesorListNewProfesor = em.merge(profesorListNewProfesor);
                    if (oldIdPersonaOfProfesorListNewProfesor != null && !oldIdPersonaOfProfesorListNewProfesor.equals(persona)) {
                        oldIdPersonaOfProfesorListNewProfesor.getProfesorList().remove(profesorListNewProfesor);
                        oldIdPersonaOfProfesorListNewProfesor = em.merge(oldIdPersonaOfProfesorListNewProfesor);
                    }
                }
            }
            for (Inscripcion inscripcionListNewInscripcion : inscripcionListNew) {
                if (!inscripcionListOld.contains(inscripcionListNewInscripcion)) {
                    Persona oldIdPersonaOfInscripcionListNewInscripcion = inscripcionListNewInscripcion.getIdPersona();
                    inscripcionListNewInscripcion.setIdPersona(persona);
                    inscripcionListNewInscripcion = em.merge(inscripcionListNewInscripcion);
                    if (oldIdPersonaOfInscripcionListNewInscripcion != null && !oldIdPersonaOfInscripcionListNewInscripcion.equals(persona)) {
                        oldIdPersonaOfInscripcionListNewInscripcion.getInscripcionList().remove(inscripcionListNewInscripcion);
                        oldIdPersonaOfInscripcionListNewInscripcion = em.merge(oldIdPersonaOfInscripcionListNewInscripcion);
                    }
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    Persona oldIdPersonaOfUsuarioListNewUsuario = usuarioListNewUsuario.getIdPersona();
                    usuarioListNewUsuario.setIdPersona(persona);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldIdPersonaOfUsuarioListNewUsuario != null && !oldIdPersonaOfUsuarioListNewUsuario.equals(persona)) {
                        oldIdPersonaOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldIdPersonaOfUsuarioListNewUsuario = em.merge(oldIdPersonaOfUsuarioListNewUsuario);
                    }
                }
            }
            for (Mensaje mensajeListNewMensaje : mensajeListNew) {
                if (!mensajeListOld.contains(mensajeListNewMensaje)) {
                    Persona oldIdDestinatarioOfMensajeListNewMensaje = mensajeListNewMensaje.getIdDestinatario();
                    mensajeListNewMensaje.setIdDestinatario(persona);
                    mensajeListNewMensaje = em.merge(mensajeListNewMensaje);
                    if (oldIdDestinatarioOfMensajeListNewMensaje != null && !oldIdDestinatarioOfMensajeListNewMensaje.equals(persona)) {
                        oldIdDestinatarioOfMensajeListNewMensaje.getMensajeList().remove(mensajeListNewMensaje);
                        oldIdDestinatarioOfMensajeListNewMensaje = em.merge(oldIdDestinatarioOfMensajeListNewMensaje);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = persona.getIdPersona();
                if (findPersona(id) == null) {
                    throw new NonexistentEntityException("The persona with id " + id + " no longer exists.");
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
            Persona persona;
            try {
                persona = em.getReference(Persona.class, id);
                persona.getIdPersona();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The persona with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TelefonoPersona> telefonoPersonaListOrphanCheck = persona.getTelefonoPersonaList();
            for (TelefonoPersona telefonoPersonaListOrphanCheckTelefonoPersona : telefonoPersonaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the TelefonoPersona " + telefonoPersonaListOrphanCheckTelefonoPersona + " in its telefonoPersonaList field has a non-nullable idPersona field.");
            }
            List<Documentosporpersona> documentosporpersonaListOrphanCheck = persona.getDocumentosporpersonaList();
            for (Documentosporpersona documentosporpersonaListOrphanCheckDocumentosporpersona : documentosporpersonaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Documentosporpersona " + documentosporpersonaListOrphanCheckDocumentosporpersona + " in its documentosporpersonaList field has a non-nullable idPersona field.");
            }
            List<Direccion> direccionListOrphanCheck = persona.getDireccionList();
            for (Direccion direccionListOrphanCheckDireccion : direccionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Direccion " + direccionListOrphanCheckDireccion + " in its direccionList field has a non-nullable idPersona field.");
            }
            List<Alumno> alumnoListOrphanCheck = persona.getAlumnoList();
            for (Alumno alumnoListOrphanCheckAlumno : alumnoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Alumno " + alumnoListOrphanCheckAlumno + " in its alumnoList field has a non-nullable idPersona field.");
            }
            List<Profesor> profesorListOrphanCheck = persona.getProfesorList();
            for (Profesor profesorListOrphanCheckProfesor : profesorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Profesor " + profesorListOrphanCheckProfesor + " in its profesorList field has a non-nullable idPersona field.");
            }
            List<Inscripcion> inscripcionListOrphanCheck = persona.getInscripcionList();
            for (Inscripcion inscripcionListOrphanCheckInscripcion : inscripcionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Inscripcion " + inscripcionListOrphanCheckInscripcion + " in its inscripcionList field has a non-nullable idPersona field.");
            }
            List<Usuario> usuarioListOrphanCheck = persona.getUsuarioList();
            for (Usuario usuarioListOrphanCheckUsuario : usuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Usuario " + usuarioListOrphanCheckUsuario + " in its usuarioList field has a non-nullable idPersona field.");
            }
            List<Mensaje> mensajeListOrphanCheck = persona.getMensajeList();
            for (Mensaje mensajeListOrphanCheckMensaje : mensajeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Mensaje " + mensajeListOrphanCheckMensaje + " in its mensajeList field has a non-nullable idDestinatario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(persona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Persona> findPersonaEntities() {
        return findPersonaEntities(true, -1, -1);
    }

    public List<Persona> findPersonaEntities(int maxResults, int firstResult) {
        return findPersonaEntities(false, maxResults, firstResult);
    }

    private List<Persona> findPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Persona.class));
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

    public Persona findPersona(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Persona.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Persona> rt = cq.from(Persona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
