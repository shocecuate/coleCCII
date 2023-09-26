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
import modelo.Menu;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Rol;
import modelo.Usuario;

/**
 *
 * @author luismorales
 */
public class RolJpaController implements Serializable {

    public RolJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rol rol) {
        if (rol.getMenuList() == null) {
            rol.setMenuList(new ArrayList<Menu>());
        }
        if (rol.getUsuarioList() == null) {
            rol.setUsuarioList(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Menu> attachedMenuList = new ArrayList<Menu>();
            for (Menu menuListMenuToAttach : rol.getMenuList()) {
                menuListMenuToAttach = em.getReference(menuListMenuToAttach.getClass(), menuListMenuToAttach.getIdMenu());
                attachedMenuList.add(menuListMenuToAttach);
            }
            rol.setMenuList(attachedMenuList);
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : rol.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getIdUsuario());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            rol.setUsuarioList(attachedUsuarioList);
            em.persist(rol);
            for (Menu menuListMenu : rol.getMenuList()) {
                Rol oldIdRolOfMenuListMenu = menuListMenu.getIdRol();
                menuListMenu.setIdRol(rol);
                menuListMenu = em.merge(menuListMenu);
                if (oldIdRolOfMenuListMenu != null) {
                    oldIdRolOfMenuListMenu.getMenuList().remove(menuListMenu);
                    oldIdRolOfMenuListMenu = em.merge(oldIdRolOfMenuListMenu);
                }
            }
            for (Usuario usuarioListUsuario : rol.getUsuarioList()) {
                Rol oldIdRolOfUsuarioListUsuario = usuarioListUsuario.getIdRol();
                usuarioListUsuario.setIdRol(rol);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldIdRolOfUsuarioListUsuario != null) {
                    oldIdRolOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldIdRolOfUsuarioListUsuario = em.merge(oldIdRolOfUsuarioListUsuario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rol rol) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rol persistentRol = em.find(Rol.class, rol.getIdRol());
            List<Menu> menuListOld = persistentRol.getMenuList();
            List<Menu> menuListNew = rol.getMenuList();
            List<Usuario> usuarioListOld = persistentRol.getUsuarioList();
            List<Usuario> usuarioListNew = rol.getUsuarioList();
            List<String> illegalOrphanMessages = null;
            for (Menu menuListOldMenu : menuListOld) {
                if (!menuListNew.contains(menuListOldMenu)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Menu " + menuListOldMenu + " since its idRol field is not nullable.");
                }
            }
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuario " + usuarioListOldUsuario + " since its idRol field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Menu> attachedMenuListNew = new ArrayList<Menu>();
            for (Menu menuListNewMenuToAttach : menuListNew) {
                menuListNewMenuToAttach = em.getReference(menuListNewMenuToAttach.getClass(), menuListNewMenuToAttach.getIdMenu());
                attachedMenuListNew.add(menuListNewMenuToAttach);
            }
            menuListNew = attachedMenuListNew;
            rol.setMenuList(menuListNew);
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getIdUsuario());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            rol.setUsuarioList(usuarioListNew);
            rol = em.merge(rol);
            for (Menu menuListNewMenu : menuListNew) {
                if (!menuListOld.contains(menuListNewMenu)) {
                    Rol oldIdRolOfMenuListNewMenu = menuListNewMenu.getIdRol();
                    menuListNewMenu.setIdRol(rol);
                    menuListNewMenu = em.merge(menuListNewMenu);
                    if (oldIdRolOfMenuListNewMenu != null && !oldIdRolOfMenuListNewMenu.equals(rol)) {
                        oldIdRolOfMenuListNewMenu.getMenuList().remove(menuListNewMenu);
                        oldIdRolOfMenuListNewMenu = em.merge(oldIdRolOfMenuListNewMenu);
                    }
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    Rol oldIdRolOfUsuarioListNewUsuario = usuarioListNewUsuario.getIdRol();
                    usuarioListNewUsuario.setIdRol(rol);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldIdRolOfUsuarioListNewUsuario != null && !oldIdRolOfUsuarioListNewUsuario.equals(rol)) {
                        oldIdRolOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldIdRolOfUsuarioListNewUsuario = em.merge(oldIdRolOfUsuarioListNewUsuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = rol.getIdRol();
                if (findRol(id) == null) {
                    throw new NonexistentEntityException("The rol with id " + id + " no longer exists.");
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
            Rol rol;
            try {
                rol = em.getReference(Rol.class, id);
                rol.getIdRol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rol with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Menu> menuListOrphanCheck = rol.getMenuList();
            for (Menu menuListOrphanCheckMenu : menuListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Rol (" + rol + ") cannot be destroyed since the Menu " + menuListOrphanCheckMenu + " in its menuList field has a non-nullable idRol field.");
            }
            List<Usuario> usuarioListOrphanCheck = rol.getUsuarioList();
            for (Usuario usuarioListOrphanCheckUsuario : usuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Rol (" + rol + ") cannot be destroyed since the Usuario " + usuarioListOrphanCheckUsuario + " in its usuarioList field has a non-nullable idRol field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(rol);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rol> findRolEntities() {
        return findRolEntities(true, -1, -1);
    }

    public List<Rol> findRolEntities(int maxResults, int firstResult) {
        return findRolEntities(false, maxResults, firstResult);
    }

    private List<Rol> findRolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rol.class));
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

    public Rol findRol(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rol.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rol> rt = cq.from(Rol.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
