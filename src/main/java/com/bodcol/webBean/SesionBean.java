package com.bodcol.webBean;

import com.bodcol.entidades.Usuario;
import java.io.Serializable;
import javax.annotation.PostConstruct;
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

     private Usuario usuario;

    @PostConstruct
    public void init(){
        usuario=new Usuario();
    }
    
    public SesionBean() {
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    
    public void verificarSesion() {
        try {
             usuario = (Usuario) context.getExternalContext().getSessionMap().get("autenticado");

            if (usuario == null) {
                context.getExternalContext().redirect("/faces/vistas/plantilla/Index.xhtml");
            }
        } catch (Exception e) {
        }
    }

    public void cerrarSesion() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }
    
        
    

}
