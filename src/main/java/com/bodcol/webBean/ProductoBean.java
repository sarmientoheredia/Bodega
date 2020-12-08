package com.bodcol.webBean;

import com.bodcol.entidades.Producto;
import com.bodcol.entidades.Rack;
import com.bodcol.entidades.Seccion;
import com.bodcol.sessionBeans.ProductoFacadeLocal;
import com.bodcol.sessionBeans.RackFacadeLocal;
import com.bodcol.sessionBeans.SeccionFacadeLocal;
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

@Named(value = "productoBean")
@ViewScoped
public class ProductoBean implements Serializable {

    //INICIO VARIABLES
    private Producto producto;
    private Seccion seccion;
    private List<Producto> productoListActivo;
    private List<Seccion> seccionList;
    private List<Rack> rackList;
    private List<Producto> productoList;
    //FIN VARIABLES

    //INICIO INYECCION
    @EJB
    private ProductoFacadeLocal productoFacadeLocal;
    @EJB
    private SeccionFacadeLocal seccionFacadeLocal;
    @EJB
    private RackFacadeLocal rackFacadeLocal;
    @Inject
    private JasperReportUtil jasperReportUtil;

    //FIN INYECCION
    public ProductoBean() {
    }

    @PostConstruct
    public void init() {
        productoList = productoFacadeLocal.findAll();
        producto = null;
        productoListActivo = productoFacadeLocal.findAllActivo();
        seccionList = seccionFacadeLocal.findAll();
    }

//    INICIO DE GETTERS Y SETTERS
    public List<Producto> getProductoList() {
        return productoList;
    }

    public void setProductoList(List<Producto> productoList) {
        this.productoList = productoList;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        rackList = rackFacadeLocal.findAll();
        this.producto = producto;
    }

    public List<Producto> getProductoListActivo() {
        return productoListActivo;
    }

    public void setProductoListActivo(List<Producto> productoListActivo) {
        this.productoListActivo = productoListActivo;
    }

    public List<Seccion> getSeccionList() {
        return seccionList;
    }

    public void setSeccionList(List<Seccion> seccionList) {
        this.seccionList = seccionList;
    }

    public List<Rack> getRackList() {
        return rackList;
    }

    public void setRackList(List<Rack> rackList) {
        this.rackList = rackList;
    }

    public Seccion getSeccion() {
        return seccion;
    }

    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }

    //FIN DE LOS GETTERS Y SETTERS
    //INICIO DE LOS METODOS
    public void nuevo() {
        producto = new Producto();
    }

    public void grabar() {
        try {
            if (producto.getId() == null) {
                productoFacadeLocal.create(producto);
                Mensaje.mostrarExito("Registro Exitoso");
            } else {
                productoFacadeLocal.edit(producto);
                Mensaje.mostrarExito("Actualización exitosa");
            }
            init();
        } catch (Exception e) {
            if (e.getCause().getCause().getClass().getName().equals("org.hibernate.exception.ConstraintViolationException")) {
                if (e.getCause().getCause().getMessage().contains("could not execute statement")) {
                    Mensaje.mostrarError("El produco ya existe");
                }
            }
        }
    }

    public void eliminar(Producto producto) {
        try {
            productoFacadeLocal.remove(producto);
            init();
            Mensaje.mostrarExito("Eliminación exitosa");
        } catch (Exception e) {
            if (e.getCause().getCause().getClass().getName().equals("javax.persistence.PersistenceException")) {
                if (e.getCause().getCause().getMessage().contains("could not execute statement")) {
                    Mensaje.mostrarError("Producto relacionado");
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
        Producto prod = (Producto) value;
        return prod.getCodigo().toLowerCase().contains(filterText)
                || prod.getNombre().toLowerCase().contains(filterText);
    }

    public void verificarCodigo() {
        Producto pro = productoFacadeLocal.findByCodigo(producto.getCodigo());
        if (pro != null) {
            Mensaje.mostrarAdvertencia("Este código ya esta registrado");
        }
    }

    public void verificarNombre() {
        Producto pro = productoFacadeLocal.findByNombre(producto.getNombre());
        if (pro != null) {
            Mensaje.mostrarAdvertencia("El producto ya existe");
        }
    }

    public void filtrarRackPorSeccion() {
        rackList = rackFacadeLocal.findAllSeccion(seccion);
    }

    //metodo para generar los reportes de los productos
    public void exportarPDF() throws Exception {
        try {
            jasperReportUtil.exportToPdf("Producto", null);
        } catch (JRException ex) {
            ex.printStackTrace(System.out);
        }
    }
    //FIN DE LOS METODOS
}
