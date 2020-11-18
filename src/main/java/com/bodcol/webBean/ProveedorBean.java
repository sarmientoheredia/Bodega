package com.bodcol.webBean;

import com.bodcol.entidades.Proveedor;
import com.bodcol.sessionBeans.ProveedorFacadeLocal;
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
    private static boolean isValid = false;

    private List<Proveedor> proveedorListActivo;

//    FIN DE LAS VARIABLES 
    //INICIO INYECCION
    @EJB
    private ProveedorFacadeLocal proveedorFacadeLocal;
    @Inject
    private JasperReportUtil jasperReportUtil;

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
                if (bandera == true) {
                    proveedorFacadeLocal.create(proveedor);
                    Mensaje.mostrarExito("Registro exitoso");
                } else {
                    Mensaje.mostrarError("Numero de documento invalido");
                }

            } else {
                proveedorFacadeLocal.edit(proveedor);
                Mensaje.mostrarExito("Actualización exitosa");
            }
            init();
        } catch (Exception e) {
            if (e.getCause().getCause().getClass().getName().equals("org.hibernate.exception.ConstraintViolationException")) {
                if (e.getCause().getCause().getMessage().contains("could not execute statement")) {
                    Mensaje.mostrarError("Proveedor ya existe");
                }
            }
        }
    }

    public void eliminar(Proveedor proveedor) {
        try {
            proveedorFacadeLocal.remove(proveedor);
            init();
            Mensaje.mostrarExito("Eliminación exitosa");
        } catch (Exception e) {
            if (e.getCause().getCause().getClass().getName().equals("javax.persistence.PersistenceException")) {
                if (e.getCause().getCause().getMessage().contains("could not execute statement")) {
                    Mensaje.mostrarError("Proveedor relacionado");
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
            Mensaje.mostrarError("Cédula invalida");
        }
    }
    //metodo para verificar si la cedula ya esta registrada

    public void verificarCedula() {
        Proveedor prov = proveedorFacadeLocal.findByCedula(proveedor.getCedula());
        if (prov != null) {
            Mensaje.mostrarAdvertencia("Cédula ya existe ");
        }
    }

    //inicio metodo para validar el ruc de unapersona juridica
    private static final int NUM_PROVINCIAS = 24;
    private static int[] coeficientes = {4, 3, 2, 7, 6, 5, 4, 3, 2};
    private static int constante = 11;

    public static Boolean operacionRuc(String ruc) {
        boolean resp_dato = false;
        final int prov = Integer.parseInt(ruc.substring(0, 2));
        if (!((prov > 0) && (prov <= NUM_PROVINCIAS))) {
            resp_dato = false;
        }

        int[] d = new int[10];
        int suma = 0;

        for (int i = 0; i < d.length; i++) {
            d[i] = Integer.parseInt(ruc.charAt(i) + "");
        }

        for (int i = 0; i < d.length - 1; i++) {
            d[i] = d[i] * coeficientes[i];
            suma += d[i];
        }

        int aux, resp;

        aux = suma % constante;
        resp = constante - aux;

        resp = (aux == 0) ? 0 : resp;

        if (resp == d[9]) {
            resp_dato = true;
        } else {
            resp_dato = false;
        }
        return resp_dato;
    }

    public void verificarRuc() {
        Proveedor prov = proveedorFacadeLocal.findByRuc(proveedor.getRuc());
        if (prov != null) {
            Mensaje.mostrarAdvertencia("Ruc ya existe ");
        }
    }

    public void validarRuc() {
        String ruc_dato = proveedor.getRuc();
        if (operacionRuc(ruc_dato)) {
            bandera = true;
            verificarRuc();
        } else {
            bandera = false;
            Mensaje.mostrarError("Ruc invalido");
        }
    }
    //fin metodo para validar el ruc de unapersona juridica

    //inicio validar el ruc de una persona natural
    public static Boolean operacionRucNatural(String cedula) {

        if (cedula == null || cedula.length() != 10) {
            isValid = false;
        }
        final int prov = Integer.parseInt(cedula.substring(0, 2));

        if (!((prov > 0) && (prov <= NUM_PROVINCIAS))) {
            isValid = false;
        }

        int[] d = new int[10];
        for (int i = 0; i < d.length; i++) {
            d[i] = Integer.parseInt(cedula.charAt(i) + "");
        }

        int imp = 0;
        int par = 0;

        for (int i = 0; i < d.length; i += 2) {
            d[i] = ((d[i] * 2) > 9) ? ((d[i] * 2) - 9) : (d[i] * 2);
            imp += d[i];
        }

        for (int i = 1; i < (d.length - 1); i += 2) {
            par += d[i];
        }

        final int suma = imp + par;

        int d10 = Integer.parseInt(String.valueOf(suma + 10).substring(0, 1)
                + "0")
                - suma;

        d10 = (d10 == 10) ? 0 : d10;

        if (d10 == d[9]) {
            isValid = true;
        } else {
            isValid = false;
        }

        return isValid;
    }

    public void verificarRucNatural() {
        Proveedor prov = proveedorFacadeLocal.findByRucNatural(proveedor.getRucNatural());
        if (prov != null) {
            Mensaje.mostrarAdvertencia("Ruc ya existe ");
        }
    }

    public void validarRucNatural() {
        String ruc_dato = proveedor.getRucNatural();
        if (operacionRucNatural(ruc_dato)) {
            bandera = true;
            verificarRucNatural();
        } else {
            bandera = false;
            Mensaje.mostrarError("Ruc invalido");
        }
    }
    //inicio validar el ruc de una persona natural

    public void exportarPDF() throws Exception {
        try {
            jasperReportUtil.exportToPdf("Proveedor", null);
        } catch (JRException ex) {
            ex.printStackTrace(System.out);
        }
    }

    //FIN DE LOS METODOS
}
