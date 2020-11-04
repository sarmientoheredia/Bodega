package com.bodcol.webBean;

import com.bodcol.entidades.Menu;
import com.bodcol.sessionBeans.MenuFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author Cbos- Com. Sarmiento H. Luis A.
 */
@Named(value = "menuBean")
@SessionScoped
public class MenuBean implements Serializable {

    @EJB
    private MenuFacadeLocal menuFacadeLocal;

    private List<Menu> menuList;
    private MenuModel menuModel;
    private Menu menu;

    public MenuBean() {
    }

    @PostConstruct
    public void init() {
        menuList = menuFacadeLocal.findAll();
        menuModel = new DefaultMenuModel();
        this.cargarMenuPrincipal();
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public MenuModel getMenuModel() {
        return menuModel;
    }

    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    private void cargarMenuPrincipal() {
        this.menuModel = new DefaultMenuModel();
        try {
            List<Menu> opcionesNivelCero = new ArrayList<>();

            opcionesNivelCero.addAll(menuList.stream()
                    .filter(op -> op.getTipo().equals('S'))
                    .distinct()
                    .collect(Collectors.toList()));

            for (Menu menu : opcionesNivelCero) {
                DefaultSubMenu submenu = new DefaultSubMenu();
                submenu.setLabel(menu.getNombre());
                this.cargarMenuHijos(menu, submenu);
                this.menuModel.addElement(submenu);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarMenuHijos(Menu opcionPadre, DefaultSubMenu menuPadre) {
        try {
            List<Menu> opcionesHijas = new ArrayList<>();

            opcionesHijas.addAll(menuList.stream()
                    .filter(op -> op.getMenuPadre() != null && op.getMenuPadre().equals(opcionPadre))
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList()));

            long nroHijos = 0;
            for (Menu opcion : opcionesHijas) {

                DefaultMenuItem itemHijo = new DefaultMenuItem();
                itemHijo.setValue(opcion.getNombre());
                itemHijo.setUrl(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getContextPath() + opcion.getUrl());
                itemHijo.setImmediate(true);
                menuPadre.getElements().add(itemHijo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public void establecerPermisos() {
//        for (Menu m : lista) {
//            if (m.getTipo().equals('S')) {
//                DefaultSubMenu firstSubmenu = new DefaultSubMenu(m.getNombre());
//                for (Menu i : lista) {
//                    Menu submenu = i.getSubMenu();
//                    if (submenu != null) {
//                        if (submenu.getId() == m.getId()) {
//                            DefaultMenuItem item = new DefaultMenuItem(i.getNombre());
//                            firstSubmenu.addElement(item);
//                        }
//                    }
//                }
//                model.addElement(firstSubmenu);
//            } else {
//                if (m.getSubMenu() == null) {
//                    DefaultMenuItem item = new DefaultMenuItem(m.getNombre());
//                    model.addElement(item);
//                }
//
//            }
//        }
//
//    }
}
