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
import modelo.Rol;
import modelo.Materiacursoporusuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.RecuperacionClave;
import modelo.Usuario;

/**
 *
 * @author luismorales
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getMateriacursoporusuarioList() == null) {
            usuario.setMateriacursoporusuarioList(new ArrayList<Materiacursoporusuario>());
        }
        if (usuario.getRecuperacionClaveList() == null) {
            usuario.setRecuperacionClaveList(new ArrayList<RecuperacionClave>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona idPersona = usuario.getIdPersona();
            if (idPersona != null) {
                idPersona = em.getReference(idPersona.getClass(), idPersona.getIdPersona());
                usuario.setIdPersona(idPersona);
            }
            Rol idRol = usuario.getIdRol();
            if (idRol != null) {
                idRol = em.getReference(idRol.getClass(), idRol.getIdRol());
                usuario.setIdRol(idRol);
            }
            List<Materiacursoporusuario> attachedMateriacursoporusuarioList = new ArrayList<Materiacursoporusuario>();
            for (Materiacursoporusuario materiacursoporusuarioListMateriacursoporusuarioToAttach : usuario.getMateriacursoporusuarioList()) {
                materiacursoporusuarioListMateriacursoporusuarioToAttach = em.getReference(materiacursoporusuarioListMateriacursoporusuarioToAttach.getClass(), materiacursoporusuarioListMateriacursoporusuarioToAttach.getIdMateriacursoporusuario());
                attachedMateriacursoporusuarioList.add(materiacursoporusuarioListMateriacursoporusuarioToAttach);
            }
            usuario.setMateriacursoporusuarioList(attachedMateriacursoporusuarioList);
            List<RecuperacionClave> attachedRecuperacionClaveList = new ArrayList<RecuperacionClave>();
            for (RecuperacionClave recuperacionClaveListRecuperacionClaveToAttach : usuario.getRecuperacionClaveList()) {
                recuperacionClaveListRecuperacionClaveToAttach = em.getReference(recuperacionClaveListRecuperacionClaveToAttach.getClass(), recuperacionClaveListRecuperacionClaveToAttach.getIdRecuperacion());
                attachedRecuperacionClaveList.add(recuperacionClaveListRecuperacionClaveToAttach);
            }
            usuario.setRecuperacionClaveList(attachedRecuperacionClaveList);
            em.persist(usuario);
            if (idPersona != null) {
                idPersona.getUsuarioList().add(usuario);
                idPersona = em.merge(idPersona);
            }
            if (idRol != null) {
                idRol.getUsuarioList().add(usuario);
                idRol = em.merge(idRol);
            }
            for (Materiacursoporusuario materiacursoporusuarioListMateriacursoporusuario : usuario.getMateriacursoporusuarioList()) {
                Usuario oldIdUsuarioOfMateriacursoporusuarioListMateriacursoporusuario = materiacursoporusuarioListMateriacursoporusuario.getIdUsuario();
                materiacursoporusuarioListMateriacursoporusuario.setIdUsuario(usuario);
                materiacursoporusuarioListMateriacursoporusuario = em.merge(materiacursoporusuarioListMateriacursoporusuario);
                if (oldIdUsuarioOfMateriacursoporusuarioListMateriacursoporusuario != null) {
                    oldIdUsuarioOfMateriacursoporusuarioListMateriacursoporusuario.getMateriacursoporusuarioList().remove(materiacursoporusuarioListMateriacursoporusuario);
                    oldIdUsuarioOfMateriacursoporusuarioListMateriacursoporusuario = em.merge(oldIdUsuarioOfMateriacursoporusuarioListMateriacursoporusuario);
                }
            }
            for (RecuperacionClave recuperacionClaveListRecuperacionClave : usuario.getRecuperacionClaveList()) {
                Usuario oldIdUsuarioOfRecuperacionClaveListRecuperacionClave = recuperacionClaveListRecuperacionClave.getIdUsuario();
                recuperacionClaveListRecuperacionClave.setIdUsuario(usuario);
                recuperacionClaveListRecuperacionClave = em.merge(recuperacionClaveListRecuperacionClave);
                if (oldIdUsuarioOfRecuperacionClaveListRecuperacionClave != null) {
                    oldIdUsuarioOfRecuperacionClaveListRecuperacionClave.getRecuperacionClaveList().remove(recuperacionClaveListRecuperacionClave);
                    oldIdUsuarioOfRecuperacionClaveListRecuperacionClave = em.merge(oldIdUsuarioOfRecuperacionClaveListRecuperacionClave);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            Persona idPersonaOld = persistentUsuario.getIdPersona();
            Persona idPersonaNew = usuario.getIdPersona();
            Rol idRolOld = persistentUsuario.getIdRol();
            Rol idRolNew = usuario.getIdRol();
            List<Materiacursoporusuario> materiacursoporusuarioListOld = persistentUsuario.getMateriacursoporusuarioList();
            List<Materiacursoporusuario> materiacursoporusuarioListNew = usuario.getMateriacursoporusuarioList();
            List<RecuperacionClave> recuperacionClaveListOld = persistentUsuario.getRecuperacionClaveList();
            List<RecuperacionClave> recuperacionClaveListNew = usuario.getRecuperacionClaveList();
            List<String> illegalOrphanMessages = null;
            for (Materiacursoporusuario materiacursoporusuarioListOldMateriacursoporusuario : materiacursoporusuarioListOld) {
                if (!materiacursoporusuarioListNew.contains(materiacursoporusuarioListOldMateriacursoporusuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Materiacursoporusuario " + materiacursoporusuarioListOldMateriacursoporusuario + " since its idUsuario field is not nullable.");
                }
            }
            for (RecuperacionClave recuperacionClaveListOldRecuperacionClave : recuperacionClaveListOld) {
                if (!recuperacionClaveListNew.contains(recuperacionClaveListOldRecuperacionClave)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RecuperacionClave " + recuperacionClaveListOldRecuperacionClave + " since its idUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPersonaNew != null) {
                idPersonaNew = em.getReference(idPersonaNew.getClass(), idPersonaNew.getIdPersona());
                usuario.setIdPersona(idPersonaNew);
            }
            if (idRolNew != null) {
                idRolNew = em.getReference(idRolNew.getClass(), idRolNew.getIdRol());
                usuario.setIdRol(idRolNew);
            }
            List<Materiacursoporusuario> attachedMateriacursoporusuarioListNew = new ArrayList<Materiacursoporusuario>();
            for (Materiacursoporusuario materiacursoporusuarioListNewMateriacursoporusuarioToAttach : materiacursoporusuarioListNew) {
                materiacursoporusuarioListNewMateriacursoporusuarioToAttach = em.getReference(materiacursoporusuarioListNewMateriacursoporusuarioToAttach.getClass(), materiacursoporusuarioListNewMateriacursoporusuarioToAttach.getIdMateriacursoporusuario());
                attachedMateriacursoporusuarioListNew.add(materiacursoporusuarioListNewMateriacursoporusuarioToAttach);
            }
            materiacursoporusuarioListNew = attachedMateriacursoporusuarioListNew;
            usuario.setMateriacursoporusuarioList(materiacursoporusuarioListNew);
            List<RecuperacionClave> attachedRecuperacionClaveListNew = new ArrayList<RecuperacionClave>();
            for (RecuperacionClave recuperacionClaveListNewRecuperacionClaveToAttach : recuperacionClaveListNew) {
                recuperacionClaveListNewRecuperacionClaveToAttach = em.getReference(recuperacionClaveListNewRecuperacionClaveToAttach.getClass(), recuperacionClaveListNewRecuperacionClaveToAttach.getIdRecuperacion());
                attachedRecuperacionClaveListNew.add(recuperacionClaveListNewRecuperacionClaveToAttach);
            }
            recuperacionClaveListNew = attachedRecuperacionClaveListNew;
            usuario.setRecuperacionClaveList(recuperacionClaveListNew);
            usuario = em.merge(usuario);
            if (idPersonaOld != null && !idPersonaOld.equals(idPersonaNew)) {
                idPersonaOld.getUsuarioList().remove(usuario);
                idPersonaOld = em.merge(idPersonaOld);
            }
            if (idPersonaNew != null && !idPersonaNew.equals(idPersonaOld)) {
                idPersonaNew.getUsuarioList().add(usuario);
                idPersonaNew = em.merge(idPersonaNew);
            }
            if (idRolOld != null && !idRolOld.equals(idRolNew)) {
                idRolOld.getUsuarioList().remove(usuario);
                idRolOld = em.merge(idRolOld);
            }
            if (idRolNew != null && !idRolNew.equals(idRolOld)) {
                idRolNew.getUsuarioList().add(usuario);
                idRolNew = em.merge(idRolNew);
            }
            for (Materiacursoporusuario materiacursoporusuarioListNewMateriacursoporusuario : materiacursoporusuarioListNew) {
                if (!materiacursoporusuarioListOld.contains(materiacursoporusuarioListNewMateriacursoporusuario)) {
                    Usuario oldIdUsuarioOfMateriacursoporusuarioListNewMateriacursoporusuario = materiacursoporusuarioListNewMateriacursoporusuario.getIdUsuario();
                    materiacursoporusuarioListNewMateriacursoporusuario.setIdUsuario(usuario);
                    materiacursoporusuarioListNewMateriacursoporusuario = em.merge(materiacursoporusuarioListNewMateriacursoporusuario);
                    if (oldIdUsuarioOfMateriacursoporusuarioListNewMateriacursoporusuario != null && !oldIdUsuarioOfMateriacursoporusuarioListNewMateriacursoporusuario.equals(usuario)) {
                        oldIdUsuarioOfMateriacursoporusuarioListNewMateriacursoporusuario.getMateriacursoporusuarioList().remove(materiacursoporusuarioListNewMateriacursoporusuario);
                        oldIdUsuarioOfMateriacursoporusuarioListNewMateriacursoporusuario = em.merge(oldIdUsuarioOfMateriacursoporusuarioListNewMateriacursoporusuario);
                    }
                }
            }
            for (RecuperacionClave recuperacionClaveListNewRecuperacionClave : recuperacionClaveListNew) {
                if (!recuperacionClaveListOld.contains(recuperacionClaveListNewRecuperacionClave)) {
                    Usuario oldIdUsuarioOfRecuperacionClaveListNewRecuperacionClave = recuperacionClaveListNewRecuperacionClave.getIdUsuario();
                    recuperacionClaveListNewRecuperacionClave.setIdUsuario(usuario);
                    recuperacionClaveListNewRecuperacionClave = em.merge(recuperacionClaveListNewRecuperacionClave);
                    if (oldIdUsuarioOfRecuperacionClaveListNewRecuperacionClave != null && !oldIdUsuarioOfRecuperacionClaveListNewRecuperacionClave.equals(usuario)) {
                        oldIdUsuarioOfRecuperacionClaveListNewRecuperacionClave.getRecuperacionClaveList().remove(recuperacionClaveListNewRecuperacionClave);
                        oldIdUsuarioOfRecuperacionClaveListNewRecuperacionClave = em.merge(oldIdUsuarioOfRecuperacionClaveListNewRecuperacionClave);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Materiacursoporusuario> materiacursoporusuarioListOrphanCheck = usuario.getMateriacursoporusuarioList();
            for (Materiacursoporusuario materiacursoporusuarioListOrphanCheckMateriacursoporusuario : materiacursoporusuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Materiacursoporusuario " + materiacursoporusuarioListOrphanCheckMateriacursoporusuario + " in its materiacursoporusuarioList field has a non-nullable idUsuario field.");
            }
            List<RecuperacionClave> recuperacionClaveListOrphanCheck = usuario.getRecuperacionClaveList();
            for (RecuperacionClave recuperacionClaveListOrphanCheckRecuperacionClave : recuperacionClaveListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the RecuperacionClave " + recuperacionClaveListOrphanCheckRecuperacionClave + " in its recuperacionClaveList field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Persona idPersona = usuario.getIdPersona();
            if (idPersona != null) {
                idPersona.getUsuarioList().remove(usuario);
                idPersona = em.merge(idPersona);
            }
            Rol idRol = usuario.getIdRol();
            if (idRol != null) {
                idRol.getUsuarioList().remove(usuario);
                idRol = em.merge(idRol);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
