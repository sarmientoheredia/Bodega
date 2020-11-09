package com.bodcol.webBean;

import com.bodcol.entidades.Rack;
import com.bodcol.sessionBeans.RackFacadeLocal;
import com.bodcol.utilitarios.Mensaje;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "rackBean")
@ViewScoped
public class RackBean implements Serializable {

    //INICO DE VARIABLES
    private List<Rack> rackList;
    private Rack rack;
    //FIN DE VARIALBLES

    //INICIO DE LA INYECCION 
    @EJB
    private RackFacadeLocal rackFacadeLocal;
    //FIN DE LA INYECCION

    public RackBean() {
    }

    @PostConstruct
    public void init() {
        rackList = rackFacadeLocal.findAll();
        rack = null;
    }

    //INICIO DE GETTERS Y SETTERS
    public List<Rack> getRackList() {
        return rackList;
    }

    public void setRackList(List<Rack> rackList) {
        this.rackList = rackList;
    }

    public Rack getRack() {
        return rack;
    }

    public void setRack(Rack rack) {
        this.rack = rack;
    }
    //FIN DE GETTERES Y SETTERS

    //INICIO DE LOS METODOS
    public void nuevo() {
        rack = new Rack();
    }

    public void grabar() {
        try {
            if (rack.getId() == null) {
                rackFacadeLocal.create(rack);
                Mensaje.mostrarExito("Registro exitoso");
            } else {
                rackFacadeLocal.edit(rack);
                Mensaje.mostrarExito("Actualización exitosa");
            }
            init();
        } catch (Exception e) {
            if (e.getCause().getCause().getClass().getName().equals("org.hibernate.exception.ConstraintViolationException")) {
                if (e.getCause().getCause().getMessage().contains("could not execute statement")) {
                    Mensaje.mostrarError("Estanteria ya existe");
                }
            }
        }
    }

    public void eliminar(Rack rack) {
        try {
            rackFacadeLocal.remove(rack);
            init();
            Mensaje.mostrarExito("Eliminación exitosa");
        } catch (Exception e) {
            if (e.getCause().getCause().getClass().getName().equals("javax.persistence.PersistenceException")) {
                if (e.getCause().getCause().getMessage().contains("could not execute statement")) {
                    Mensaje.mostrarError("Estantería relacionada");
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

        Rack rac = (Rack) value;
        return rac.getNombre().toLowerCase().contains(filterText)
                || rac.getSeccion().getNombre().toLowerCase().contains(filterText)
                || rac.getId() == filterInt;
    }

    //METODO PARA CONVERTIR EL ID
    private int getInteger(String string) {
        try {
            return Integer.valueOf(string);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    //metodo para validar el nombre ya no exista en la base de datos 
    public void verificarNombre() {
        Rack rac = rackFacadeLocal.findByNombre(rack.getNombre());
        if (rac != null) {
            Mensaje.mostrarAdvertencia("Estantería ya existe");
        }
    }

}
