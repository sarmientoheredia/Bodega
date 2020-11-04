package com.bodcol.webBean;

import com.bodcol.entidades.Usuario;
import com.bodcol.sessionBeans.UsuarioFacadeLocal;
import com.bodcol.utilitarios.Mensaje;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;

@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    //INICIO DE LAS VARIABLES
    private Usuario usuario;
    //FIN DE LAS VARIABLES

    //INICIO DE LA INYECCION
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;

    //FIN DE LA INYECCION
    public LoginBean() {
    }

    @PostConstruct
    public void init() {
        usuario = new Usuario();
    }

    //INICO DE LOS GETTERES Y SETTERS
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    //FIN DE LOS GETTERS Y SETTERS

    //INICIO DE LOS METODOS 
    public String iniciarSecion() {
        Usuario us;
        String redireccion = null;
        try {
            us = usuarioFacadeLocal.iniciarSesion(usuario);
            if (us != null) {
                //hay que almacenar en la session del jsf para podre cerrar la sesion
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("autenticado", us);
                
                redireccion = "/vistas/plantilla/Inicio?faces-redirect=true";
            }else{
                Mensaje.mostrarAdvertencia("Credenciales Incorectas");
            }

        } catch (Exception e) {
            Mensaje.mostrarError("Error de Autenticacion");
        }
        return redireccion;
    }

 
    
    
    
    
    
    //FIN DE LOS METODOS
}
