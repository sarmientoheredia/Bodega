package com.bodcol.webBean;

import com.bodcol.entidades.Usuario;
import com.bodcol.sessionBeans.UsuarioFacadeLocal;
import com.bodcol.utilitarios.Mensaje;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

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
    public void init(){
        usuario=new Usuario();
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
    public String iniciarSecion(){
        String redireccion=null;
        try {
          usuarioFacadeLocal.iniciarSesion(usuario);
           redireccion= "/vistas/plantilla/Inicio";
        } catch (Exception e) {
            Mensaje.mostrarError("Error de Autenticacion");
        }
        return  redireccion;
    }
    
    
    
    //FIN DE LOS METODOS
    
}
