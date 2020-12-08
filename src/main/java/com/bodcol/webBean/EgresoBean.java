package com.bodcol.webBean;

import com.bodcol.entidades.DetalleEgreso;
import com.bodcol.entidades.Egreso;
import com.bodcol.entidades.Producto;
import com.bodcol.entidades.Usuario;
import com.bodcol.sessionBeans.EgresoFacadeLocal;
import com.bodcol.utilitarios.JasperReportUtil;
import com.bodcol.utilitarios.Mensaje;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import net.sf.jasperreports.engine.JRException;

@Named(value = "egresoBean")
@ViewScoped
public class EgresoBean implements Serializable {

    //INICIO DE LAS VARIABLES
    private List<Egreso> egresoList;
    private Egreso egreso;
    private long numero;
    private DetalleEgreso detalleEgreso;
    private Date fechaInicio;
    private Date fechaFin;
    private List<Egreso> imprimirEgreso;
    //FIN DE LAS VARIABLES

    //INICIO DE LA INYECCION
    @EJB
    private EgresoFacadeLocal egresoFacadeLocal;
    @Inject
    private JasperReportUtil jasperReportUtil;

    //FIN DE LA INYECCION
    public EgresoBean() {
    }

    @PostConstruct
    public void init() {
        egresoList = egresoFacadeLocal.findAll();
        egreso = null;
    }

    //INICIO DE LOS GETTERS Y SETTERS
    public List<Egreso> getEgresoList() {
        return egresoList;
    }

    public void setEgresoList(List<Egreso> egresoList) {
        this.egresoList = egresoList;
    }

    public Egreso getEgreso() {
        return egreso;
    }

    public void setEgreso(Egreso egreso) {
        this.egreso = egreso;
    }

    public DetalleEgreso getDetalleEgreso() {
        return detalleEgreso;
    }

    public void setDetalleEgreso(DetalleEgreso detalleEgreso) {
        this.detalleEgreso = detalleEgreso;
    }

    public void setBodeguero(Usuario bodeguero) {
        egreso.setUsuario(bodeguero);
    }

    public void setLogistico(Usuario logistico) {
        egreso.setUsuario2(logistico);
    }

    public void setSolicita(Usuario solicita) {
        egreso.setUsuario1(solicita);
    }

    public void setProducto(Producto producto) {
        detalleEgreso.setProducto(producto);
    }

    public long getNumero() {
        return numero;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public List<Egreso> getImprimirEgreso() {
        return imprimirEgreso;
    }

    public void setImprimirEgreso(List<Egreso> imprimirEgreso) {
        this.imprimirEgreso = imprimirEgreso;
    }
    //FIN GETTERS Y SETTERS

    //INICIO DE LOS METODOS 
    public void nuevo() {
        egreso = new Egreso();
        detalleEgreso = new DetalleEgreso();
        numero = egresoList.stream().count() + 1;
    }

    //metodo para seleccionar e instanciar 
    public void seleccionarEgreso(Egreso egreso) {
        this.egreso = egreso;
        detalleEgreso = new DetalleEgreso();
    }

    public void grabar() {
        try {
            if (egreso.getId() == null) {
                egresoFacadeLocal.create(egreso);
                Mensaje.mostrarExito("Registro Exitoso");
            } else {
                egresoFacadeLocal.edit(egreso);
                Mensaje.mostrarExito("Actualización exitosa");
            }
//            init();
        } catch (Exception e) {
            if (e.getCause().getCause().getClass().getName().equals("org.hibernate.exception.ConstraintViolationException")) {
                if (e.getCause().getCause().getMessage().contains("could not execute statement")) {
                    Mensaje.mostrarError("Numero de ingreso existente");
                }
            }
        }
    }

    public void eliminar(Egreso egreso) {
        try {
            egresoFacadeLocal.remove(egreso);
            init();
            Mensaje.mostrarExito("Eliminación exitosa");
        } catch (Exception e) {
            if (e.getCause().getCause().getClass().getName().equals("javax.persistence.PersistenceException")) {
                if (e.getCause().getCause().getMessage().contains("could not execute statement")) {
                    Mensaje.mostrarError("Ingreso relacionado");
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
        Egreso egr = (Egreso) value;
        return egr.getDependencia().toLowerCase().contains(filterText);
    }

    //metodo para agregar a la lista del detalle y validar si no hay otro producto
    public void agregarDetalle() {
        if (egreso.esProductoDuplicado(detalleEgreso.getProducto())) {
            Mensaje.mostrarAdvertencia("El producto ya esta en el detalle");
            return;
        }
        egreso.agregarDetalle(detalleEgreso);
        detalleEgreso = new DetalleEgreso();
        Mensaje.mostrarExito("Producto agregado al detalle");

    }

    public void exportarPDFParam(Egreso egreso) {

        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("egresoId", egreso.getId());
            jasperReportUtil.exportToPdf("Egreso", parametros);
        } catch (Exception ex) {
            Mensaje.mostrarError("Error al generar el reporte");
        }
    }

    public void comprobarStock() {
        if (detalleEgreso.getCantidad().compareTo(detalleEgreso.getProducto().getStock()) < 0) {
            Mensaje.mostrarExito("La cantidad es valida");
        } else if (detalleEgreso.getCantidad().compareTo(detalleEgreso.getProducto().getStock()) == 0) {
            Mensaje.mostrarAdvertencia("La cantidad es igual al stock");
        } else if (detalleEgreso.getCantidad().compareTo(detalleEgreso.getProducto().getStock()) > 0) {
            Mensaje.mostrarError("La cantidad es mayor al stock");
        }
    }

    public void filtrarEgreso() {
        imprimirEgreso = egresoFacadeLocal.findDate(fechaInicio, fechaFin);
    }

    public void exportarPDFFiltrado() throws Exception {
        try {
            jasperReportUtil.exportToPdf("EgresoFiltrado", null, imprimirEgreso);
        } catch (JRException ex) {
            ex.printStackTrace(System.out);
        }
    }

    public void limpiar() {
        fechaInicio = null;
        fechaFin = null;
        imprimirEgreso = null;
        Mensaje.mostrarExito("Se limpiaron los campos");
    }
//FIN DE LOS METODOS
}
