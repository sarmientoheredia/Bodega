package com.bodcol.webBean;

import com.bodcol.entidades.Usuario;
import com.bodcol.sessionBeans.UsuarioFacadeLocal;
import com.bodcol.utilitarios.Encriptar;
import com.bodcol.utilitarios.JasperReportUtil;
import com.bodcol.utilitarios.Mensaje;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import net.sf.jasperreports.engine.JRException;

@Named(value = "usuarioBean")
@ViewScoped
public class UsuarioBean implements Serializable {

//    INICIO DE LAS VARIABLES
    private List<Usuario> usuarioList;
    private List<Usuario> usuarioListCargo;
    private Usuario usuario;
    private boolean bandera;
//    FIN DE LAS VARIABLES

//    INICIO INYECCION
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @Inject
    private JasperReportUtil jasperReportUtil;
//    FIN INYECCION

    public UsuarioBean() {
    }

    @PostConstruct
    public void init() {
        usuarioList = usuarioFacadeLocal.findAll();
        usuarioListCargo = usuarioFacadeLocal.findAllCargo();
        usuario = null;
    }

//    INICO GETTERS Y SETTERS
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getUsuarioListCargo() {
        return usuarioListCargo;
    }

    public void setUsuarioListCargo(List<Usuario> usuarioListCargo) {
        this.usuarioListCargo = usuarioListCargo;
    }

//    FIN GETTERS Y SETTERS
//    INICO DE LOS METODOS
    public void nuevo() {
        this.usuario = new Usuario();
    }

    public void grabar() {
        try {
            if (usuario.getId() == null) {
                if (bandera == true) {
                    usuario.setUsuario(usuario.getCedula());
                    usuario.setPassword(Encriptar.sha512(usuario.getCedula()));
                    usuarioFacadeLocal.create(usuario);
                    Mensaje.mostrarExito("Registro Exitoso");
                } else {
                    Mensaje.mostrarError("Cedula invalida");
                }

            } else {
                usuarioFacadeLocal.edit(usuario);
                Mensaje.mostrarExito("Actualizacion exitosa");
            }
            init();

        } catch (Exception e) {
            if (e.getCause().getCause().getClass().getName().equals("org.hibernate.exception.ConstraintViolationException")) {
                if (e.getCause().getCause().getMessage().contains("could not execute statement")) {
                    Mensaje.mostrarError("Cedula ya existe");
                }
            }
        }

    }

    public void eliminar(Usuario usuario) {
        try {
            usuarioFacadeLocal.remove(usuario);
            init();
            Mensaje.mostrarExito("Eliminacion exitosa");
        } catch (Exception e) {
            if (e.getCause().getCause().getClass().getName().equals("javax.persistence.PersistenceException")) {
                if (e.getCause().getCause().getMessage().contains("could not execute statement")) {
                    Mensaje.mostrarError("Usuario mantiene relaciones");
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

        Usuario us = (Usuario) value;
        return us.getCedula().toLowerCase().contains(filterText)
                || us.getGrado().toLowerCase().contains(filterText)
                || us.getArma().toLowerCase().contains(filterText)
                || us.getNombreCompleto().toLowerCase().contains(filterText);
    }

    //METODO PARA CONVERTIR EL ID
    private int getInteger(String string) {
        try {
            return Integer.valueOf(string);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    //metodo para validar el numero de la cedula del cliente
    public void operacionCedula() {
        int c, suma = 0, acum, resta = 0;

        for (int i = 0; i < usuario.getCedula().length() - 1; i++) {
            c = Integer.parseInt(usuario.getCedula().charAt(i) + "");
            if (i % 2 == 0) {
                c = c * 2;
                if (c > 9) {
                    c = c - 9;
                }
            }
            suma = suma + c;
        }
        if (suma % 10 != 0) {
            acum = ((suma / 10) + 1) * 10;
            resta = acum - suma;
        }
        int ultimo = Integer.parseInt(usuario.getCedula().charAt(9) + "");
        if (ultimo == resta) {
            verificarCedula();
            bandera = true;

        } else {
            bandera = false;
            Mensaje.mostrarError("Cedula invalida");
        }
    }
    //metodo para verificar si la cedula ya esta registrada

    public void verificarCedula() {
        Usuario usu = usuarioFacadeLocal.findByCedula(usuario.getCedula());
        if (usu != null) {
            Mensaje.mostrarAdvertencia("Cedula ya existe");
        }
    }

    //como llamar a un metodo que esta en otro bean
    public void exportarPDF() throws Exception {
        try {
            jasperReportUtil.exportToPdf("Usuario", null);
        } catch (JRException ex) {
            ex.printStackTrace(System.out);
        }
    }
//    FIN DE LOS METODOS
}
