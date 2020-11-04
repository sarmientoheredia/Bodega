package com.bodcol.webBean;

import com.bodcol.entidades.Producto;
import com.bodcol.sessionBeans.ProductoFacadeLocal;
import com.bodcol.utilitarios.Mensaje;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "productoBean")
@ViewScoped
public class ProductoBean implements Serializable {

    //INICIO VARIABLES
    private List<Producto> productoList;
    private Producto producto;
    private List<Producto> productoListActivo;
    //FIN VARIABLES

    //INICIO INYECCION
    @EJB
    private ProductoFacadeLocal productoFacadeLocal;
    //FIN INYECCION

    public ProductoBean() {
    }

    @PostConstruct
    public void init() {
        productoList = productoFacadeLocal.findAll();
        producto = null;
        productoListActivo=productoFacadeLocal.findAllActivo();
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
        this.producto = producto;
    }

    public List<Producto> getProductoListActivo() {
        return productoListActivo;
    }

    public void setProductoListActivo(List<Producto> productoListActivo) {
        this.productoListActivo = productoListActivo;
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
                Mensaje.mostrarExito("Actualizacion exitosa");
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

    public void eliminar(Producto producto) {
        try {
            productoFacadeLocal.remove(producto);
            init();
            Mensaje.mostrarExito("Eliminacion exitosa");
        } catch (Exception e) {
            if (e.getCause().getCause().getClass().getName().equals("javax.persistence.PersistenceException")) {
                if (e.getCause().getCause().getMessage().contains("could not execute statement")) {
                    Mensaje.mostrarError("No se puede eliminar un producto que tiene relacion");
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

    public void verificarNombre() {
            Producto pro = productoFacadeLocal.findByNombre(producto.getNombre());
            if (pro != null) {
                Mensaje.mostrarAdvertencia("El Producto ya existe");
            }
        }

    //FIN DE LOS METODOS
}
