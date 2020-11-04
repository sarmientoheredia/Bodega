package com.bodcol.webBean;

import com.bodcol.entidades.Proveedor;
import com.bodcol.sessionBeans.ProveedorFacadeLocal;
import com.bodcol.utilitarios.Mensaje;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Cbos- Com. Sarmiento H. Luis A.
 */
@Named(value = "proveedorBean")
@ViewScoped
public class ProveedorBean implements Serializable {

//    INICIO DE LAS VARIABLES 
    private List<Proveedor> proveedorList;
    private Proveedor proveedor;
    private boolean actCedula;
    private boolean actRucNatural;
    private boolean actRuc;
    private boolean bandera;

    private List<Proveedor> proveedorListActivo;

//    FIN DE LAS VARIABLES 
    //INICIO INYECCION
    @EJB
    private ProveedorFacadeLocal proveedorFacadeLocal;

    //FIN INYECCION
    public ProveedorBean() {
    }

    @PostConstruct
    public void init() {
        proveedorList = proveedorFacadeLocal.findAll();
        proveedor = null;
        proveedorListActivo = proveedorFacadeLocal.findAllActivo();

        actCedula = false;
        actRucNatural = false;
        actRuc = false;
    }

    //INICIO GETTERS Y SETTERS
    public List<Proveedor> getProveedorList() {
        return proveedorList;
    }

    public void setProveedorList(List<Proveedor> proveedorList) {
        this.proveedorList = proveedorList;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public boolean isActCedula() {
        return actCedula;
    }

    public void setActCedula(boolean actCedula) {
        this.actCedula = actCedula;
    }

    public boolean isActRucNatural() {
        return actRucNatural;
    }

    public void setActRucNatural(boolean actRucNatural) {
        this.actRucNatural = actRucNatural;
    }

    public boolean isActRuc() {
        return actRuc;
    }

    public void setActRuc(boolean actRuc) {
        this.actRuc = actRuc;
    }

    public boolean isBandera() {
        return bandera;
    }

    public void setBandera(boolean bandera) {
        this.bandera = bandera;
    }

    public List<Proveedor> getProveedorListActivo() {
        return proveedorListActivo;
    }

    public void setProveedorListActivo(List<Proveedor> proveedorListActivo) {
        this.proveedorListActivo = proveedorListActivo;
    }

    //FIN GETTERS Y SETTERS
    //INICIO DE LOS METODOS
    public void seleccionarProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
        acivarCampos();
    }

    public void nuevo() {
        proveedor = new Proveedor();
    }

    public void grabar() {
        try {
            if (proveedor.getId() == null) {
                proveedorFacadeLocal.create(proveedor);
                Mensaje.mostrarExito("Registro Exitoso");
            } else {
                proveedorFacadeLocal.edit(proveedor);
                Mensaje.mostrarExito("Actualizacion Exitosa");
            }
            init();
        } catch (Exception e) {
            System.out.println("primer nivel: " + e.getMessage());
            System.out.println("primera clase: " + e.getClass().getName());

            System.out.println("segundo nivel: " + e.getCause().getMessage());
            System.out.println("primera clase: " + e.getCause().getClass().getName());

            System.out.println("tercer nivel: " + e.getCause().getCause().getMessage());
            System.out.println("primera clase: " + e.getCause().getCause().getClass().getName());

            if (e.getCause().getCause().getClass().getName().equals("org.hibernate.exception.ConstraintViolationException")) {
                if (e.getCause().getCause().getMessage().contains("could not execute statement")) {
                    Mensaje.mostrarError("La categoria ya esta registrado");
                }
            }
        }
    }

    public void eliminar(Proveedor proveedor) {
        try {
            proveedorFacadeLocal.remove(proveedor);
            init();
            Mensaje.mostrarExito("Proveedor Eliminado");
        } catch (Exception e) {
            if (e.getCause().getCause().getClass().getName().equals("javax.persistence.PersistenceException")) {
                if (e.getCause().getCause().getMessage().contains("could not execute statement")) {
                    Mensaje.mostrarError("El proveedor esta relacionado");
                }
            }
        }
    }

    //METODO PARA FILTRAR POR CUALQUIER CAMPO
    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (filterText == null || filterText.equals("")) {
            return true;
        }
        Proveedor prov = (Proveedor) value;
        return prov.getDireccion().toLowerCase().contains(filterText)
                || prov.getTelefono().toLowerCase().contains(filterText)
                || prov.getNombre().toLowerCase().contains(filterText)
                || prov.getCelular().toLowerCase().contains(filterText)
                || prov.getCorreo().toLowerCase().contains(filterText);
    }

    public void acivarCampos() {
        if (proveedor.getDocumento() == 1) {
            actCedula = true;
            actRucNatural = false;
            actRuc = false;
        } else if (proveedor.getDocumento() == 2) {
            actCedula = false;
            actRucNatural = true;
            actRuc = false;
        } else if (proveedor.getDocumento() == 3) {
            actCedula = false;
            actRucNatural = false;
            actRuc = true;
        }
    }

    //metodo para validar el numero de la cedula del cliente
    public void operacionCedula() {
        int c, suma = 0, acum, resta = 0;

        for (int i = 0; i < proveedor.getCedula().length() - 1; i++) {
            c = Integer.parseInt(proveedor.getCedula().charAt(i) + "");
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
        int ultimo = Integer.parseInt(proveedor.getCedula().charAt(9) + "");
        if (ultimo == resta) {
            verificarCedula();
            bandera = true;

        } else {
            bandera = false;
            Mensaje.mostrarError("El numero de cedula invalido");
        }
    }
    //metodo para verificar si la cedula ya esta registrada

    public void verificarCedula() {
        Proveedor usu = proveedorFacadeLocal.findByCedula(proveedor.getCedula());
        if (usu != null) {
            Mensaje.mostrarAdvertencia("Este numero ya existe");
        }
    }
    //FIN DE LOS METODOS
}
