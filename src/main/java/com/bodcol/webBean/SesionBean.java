package com.bodcol.webBean;

import com.bodcol.entidades.Usuario;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Cbos- Com. Sarmiento H. Luis A.
 */

/***/
@Named(value = "sesionBean")
@ViewScoped
public class SesionBean implements Serializable {

    FacesContext context = FacesContext.getCurrentInstance();

 
    
    public SesionBean() {
    }

    public void verificarSesion() {
        try {
            Usuario us = (Usuario) context.getExternalContext().getSessionMap().get("autenticado");

            if (us == null) {
                context.getExternalContext().redirect("/Bodega/faces/vistas/plantilla/Index.xhtml");
            }
        } catch (Exception e) {
        }
    }

    public void cerrarSesion() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }
}
