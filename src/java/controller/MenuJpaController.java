/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Menu;
import modelo.Rol;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author luismorales
 */
public class MenuJpaController implements Serializable {

    public MenuJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Menu menu) {
        if (menu.getMenuList() == null) {
            menu.setMenuList(new ArrayList<Menu>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Menu idPadre = menu.getIdPadre();
            if (idPadre != null) {
                idPadre = em.getReference(idPadre.getClass(), idPadre.getIdMenu());
                menu.setIdPadre(idPadre);
            }
            Rol idRol = menu.getIdRol();
            if (idRol != null) {
                idRol = em.getReference(idRol.getClass(), idRol.getIdRol());
                menu.setIdRol(idRol);
            }
            List<Menu> attachedMenuList = new ArrayList<Menu>();
            for (Menu menuListMenuToAttach : menu.getMenuList()) {
                menuListMenuToAttach = em.getReference(menuListMenuToAttach.getClass(), menuListMenuToAttach.getIdMenu());
                attachedMenuList.add(menuListMenuToAttach);
            }
            menu.setMenuList(attachedMenuList);
            em.persist(menu);
            if (idPadre != null) {
                idPadre.getMenuList().add(menu);
                idPadre = em.merge(idPadre);
            }
            if (idRol != null) {
                idRol.getMenuList().add(menu);
                idRol = em.merge(idRol);
            }
            for (Menu menuListMenu : menu.getMenuList()) {
                Menu oldIdPadreOfMenuListMenu = menuListMenu.getIdPadre();
                menuListMenu.setIdPadre(menu);
                menuListMenu = em.merge(menuListMenu);
                if (oldIdPadreOfMenuListMenu != null) {
                    oldIdPadreOfMenuListMenu.getMenuList().remove(menuListMenu);
                    oldIdPadreOfMenuListMenu = em.merge(oldIdPadreOfMenuListMenu);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Menu menu) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Menu persistentMenu = em.find(Menu.class, menu.getIdMenu());
            Menu idPadreOld = persistentMenu.getIdPadre();
            Menu idPadreNew = menu.getIdPadre();
            Rol idRolOld = persistentMenu.getIdRol();
            Rol idRolNew = menu.getIdRol();
            List<Menu> menuListOld = persistentMenu.getMenuList();
            List<Menu> menuListNew = menu.getMenuList();
            if (idPadreNew != null) {
                idPadreNew = em.getReference(idPadreNew.getClass(), idPadreNew.getIdMenu());
                menu.setIdPadre(idPadreNew);
            }
            if (idRolNew != null) {
                idRolNew = em.getReference(idRolNew.getClass(), idRolNew.getIdRol());
                menu.setIdRol(idRolNew);
            }
            List<Menu> attachedMenuListNew = new ArrayList<Menu>();
            for (Menu menuListNewMenuToAttach : menuListNew) {
                menuListNewMenuToAttach = em.getReference(menuListNewMenuToAttach.getClass(), menuListNewMenuToAttach.getIdMenu());
                attachedMenuListNew.add(menuListNewMenuToAttach);
            }
            menuListNew = attachedMenuListNew;
            menu.setMenuList(menuListNew);
            menu = em.merge(menu);
            if (idPadreOld != null && !idPadreOld.equals(idPadreNew)) {
                idPadreOld.getMenuList().remove(menu);
                idPadreOld = em.merge(idPadreOld);
            }
            if (idPadreNew != null && !idPadreNew.equals(idPadreOld)) {
                idPadreNew.getMenuList().add(menu);
                idPadreNew = em.merge(idPadreNew);
            }
            if (idRolOld != null && !idRolOld.equals(idRolNew)) {
                idRolOld.getMenuList().remove(menu);
                idRolOld = em.merge(idRolOld);
            }
            if (idRolNew != null && !idRolNew.equals(idRolOld)) {
                idRolNew.getMenuList().add(menu);
                idRolNew = em.merge(idRolNew);
            }
            for (Menu menuListOldMenu : menuListOld) {
                if (!menuListNew.contains(menuListOldMenu)) {
                    menuListOldMenu.setIdPadre(null);
                    menuListOldMenu = em.merge(menuListOldMenu);
                }
            }
            for (Menu menuListNewMenu : menuListNew) {
                if (!menuListOld.contains(menuListNewMenu)) {
                    Menu oldIdPadreOfMenuListNewMenu = menuListNewMenu.getIdPadre();
                    menuListNewMenu.setIdPadre(menu);
                    menuListNewMenu = em.merge(menuListNewMenu);
                    if (oldIdPadreOfMenuListNewMenu != null && !oldIdPadreOfMenuListNewMenu.equals(menu)) {
                        oldIdPadreOfMenuListNewMenu.getMenuList().remove(menuListNewMenu);
                        oldIdPadreOfMenuListNewMenu = em.merge(oldIdPadreOfMenuListNewMenu);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = menu.getIdMenu();
                if (findMenu(id) == null) {
                    throw new NonexistentEntityException("The menu with id " + id + " no longer exists.");
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
            Menu menu;
            try {
                menu = em.getReference(Menu.class, id);
                menu.getIdMenu();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The menu with id " + id + " no longer exists.", enfe);
            }
            Menu idPadre = menu.getIdPadre();
            if (idPadre != null) {
                idPadre.getMenuList().remove(menu);
                idPadre = em.merge(idPadre);
            }
            Rol idRol = menu.getIdRol();
            if (idRol != null) {
                idRol.getMenuList().remove(menu);
                idRol = em.merge(idRol);
            }
            List<Menu> menuList = menu.getMenuList();
            for (Menu menuListMenu : menuList) {
                menuListMenu.setIdPadre(null);
                menuListMenu = em.merge(menuListMenu);
            }
            em.remove(menu);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Menu> findMenuEntities() {
        return findMenuEntities(true, -1, -1);
    }

    public List<Menu> findMenuEntities(int maxResults, int firstResult) {
        return findMenuEntities(false, maxResults, firstResult);
    }

    private List<Menu> findMenuEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Menu.class));
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

    public Menu findMenu(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Menu.class, id);
        } finally {
            em.close();
        }
    }

    public int getMenuCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Menu> rt = cq.from(Menu.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
