package com.bodcol.webBean;

import com.bodcol.entidades.Seccion;
import com.bodcol.sessionBeans.SeccionFacadeLocal;
import com.bodcol.utilitarios.Mensaje;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "seccionBean")
@ViewScoped
public class SeccionBean implements Serializable {

    //INICIO DE LAS VARIABLES
    private List<Seccion> seccionList;
    private Seccion seccion;
    //FIN DE LAS VARIABLES

    //INICIO DE LA INYECCION
    @EJB
    private SeccionFacadeLocal seccionFacadeLocal;
    //FIN DE LA INYECCION

    public SeccionBean() {
    }

    @PostConstruct
    public void init() {
        seccionList = seccionFacadeLocal.findAll();
        seccion = null;
    }

    //INICIO DE LOS METODOS GETTERS Y SETTERS
    public List<Seccion> getSeccionList() {
        return seccionList;
    }

    public void setSeccionList(List<Seccion> seccionList) {
        this.seccionList = seccionList;
    }

    public Seccion getSeccion() {
        return seccion;
    }

    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }
    //FIN DE LOS METODOS SETERS Y GETTERS   

    public void nuevo() {
        seccion = new Seccion();
    }

    public void grabar() {
        try {
            if (seccion.getId() == null) {
                seccionFacadeLocal.create(seccion);
                Mensaje.mostrarExito("Registro exitoso");
            } else {
                seccionFacadeLocal.edit(seccion);
                Mensaje.mostrarExito("Actualizacion Exitosa");
            }
            init();
        } catch (Exception e) {
            if (e.getCause().getCause().getClass().getName().equals("org.hibernate.exception.ConstraintViolationException")) {
                if (e.getCause().getCause().getMessage().contains("could not execute statement")) {
                    Mensaje.mostrarError("Esta Seccion ya Existe");
                }
            }
        }
    }

    public void eliminar(Seccion seccion) {
        try {
            seccionFacadeLocal.remove(seccion);
            init();
            Mensaje.mostrarExito("Eliminacion exitosa");
        } catch (Exception e) {
            System.out.println("mensaje nivel 1: " + e.getMessage());
            System.out.println("clase nivel 1: " + e.getClass().getName());

            System.out.println("mensaje segundo nivel: " + e.getCause().getMessage());
            System.out.println("clase nivel 2: " + e.getCause().getClass().getName());

            System.out.println("mensaje tercer nivel: " + e.getCause().getCause().getMessage());
            System.out.println("clase nivel 3: " + e.getCause().getCause().getClass().getName());

            if (e.getCause().getCause().getClass().getName().equals("javax.persistence.PersistenceException")) {
                if (e.getCause().getCause().getMessage().contains("could not execute statement")) {
                    Mensaje.mostrarError("Existen productos que tienen esta categoria");
                }
            }
        }
    }

    //metdod para hacer la busqueda dinamica
    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (filterText == null || filterText.equals("")) {
            return true;
        }
        int filterInt = getInteger(filterText);

        Seccion sec = (Seccion) value;
        return sec.getNombre().toLowerCase().contains(filterText)
                || sec.getId() == filterInt;
    }

    //METODO PARA CONVERTIR EL ID
    private int getInteger(String string) {
        try {
            return Integer.valueOf(string);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    //metodo para verificar el nombre de la seccion
    public void verificarNombre() {
        Seccion sec = seccionFacadeLocal.findByNombre(seccion.getNombre());
        if (sec != null) {
            Mensaje.mostrarAdvertencia("La categoria ya existe");
        }
    }
}
