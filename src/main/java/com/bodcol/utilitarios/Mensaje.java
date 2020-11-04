package com.bodcol.utilitarios;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Mensaje {
    
    //metodo para mostrar los mensajes el estatico nos permite usar si necesisdad de instanciar las clases
    public static void mostrarExito(String mensaje) {
        mostrarMensaje(mensaje, FacesMessage.SEVERITY_INFO);
    }

    public static void mostrarError(String mensaje) {
        mostrarMensaje(mensaje, FacesMessage.SEVERITY_ERROR);
    }

    public static void mostrarAdvertencia(String mensaje) {
        mostrarMensaje(mensaje, FacesMessage.SEVERITY_WARN);
    }

    private static void mostrarMensaje(String mensaje, FacesMessage.Severity severidad) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severidad, mensaje, mensaje));
    }
}
