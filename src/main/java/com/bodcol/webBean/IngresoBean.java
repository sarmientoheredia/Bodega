package com.bodcol.webBean;

import com.bodcol.entidades.DetalleIngreso;
import com.bodcol.entidades.Ingreso;
import com.bodcol.entidades.Proveedor;
import com.bodcol.entidades.Usuario;
import com.bodcol.sessionBeans.IngresoFacadeLocal;
import com.bodcol.sessionBeans.UsuarioFacadeLocal;
import com.bodcol.utilitarios.Mensaje;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "ingresoBean")
@ViewScoped
public class IngresoBean implements Serializable {

    //INICIO DE LAS VARIABLES
    private List<Ingreso> ingresoList;
    private Ingreso ingreso;
    private Usuario bodeguero;
    private Usuario logistico;
    private Usuario compras;
    private Proveedor proveedor;

    private List<Usuario> usuarioListCargo;
    
    private DetalleIngreso detalleIngreso;
    //FIN DE LAS VARIABLES

    //INICIO DE LA INYECCION
    @EJB
    private IngresoFacadeLocal ingresoFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;

       
    //FIN DE LA INYECCION
    public IngresoBean() {
    }

    @PostConstruct
    public void init() {
        ingresoList = ingresoFacadeLocal.findAll();
        ingreso = null;
    }

    //INICIO DE LOS GETTERS Y SETTERS
    public List<Ingreso> getIngresoList() {
        return ingresoList;
    }

    public void setIngresoList(List<Ingreso> ingresoList) {
        this.ingresoList = ingresoList;
    }

    public Ingreso getIngreso() {
        return ingreso;
    }

    public void setIngreso(Ingreso ingreso) {
        this.ingreso = ingreso;
    }

    public DetalleIngreso getDetalleIngreso() {
        return detalleIngreso;
    }

    public void setDetalleIngreso(DetalleIngreso detalleIngreso) {
        this.detalleIngreso = detalleIngreso;
    }
    
       public Usuario getBodeguero() {
        return bodeguero;
    }

    public void setBodeguero(Usuario bodeguero) {
        ingreso.setUsuario2(bodeguero);
    }

    public Usuario getLogistico() {
        return logistico;
    }

    public void setLogistico(Usuario logistico) {
        ingreso.setUsuario1(logistico);
    }

    public Usuario getCompras() {
        return compras;
    }

    public void setCompras(Usuario compras) {
        ingreso.setUsuario(compras);
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        ingreso.setProveedor(proveedor);
    }
    //FIN GETTERS Y SETTERS

    //INICIO DE LOS METODOS 
    public void nuevo() {
        ingreso = new Ingreso();
        detalleIngreso = new DetalleIngreso();
    }

    //metodo para seleccionar e instanciar 
    public void seleccionarIngreso(Ingreso ingreso) {
        this.ingreso = ingreso;
        detalleIngreso = new DetalleIngreso();
    }

    public void grabar() {
        try {
            if (ingreso.getId() == null) {
               
                ingresoFacadeLocal.create(ingreso);
                Mensaje.mostrarExito("Registro Exitoso");
            } else {
                ingresoFacadeLocal.edit(ingreso);
                Mensaje.mostrarExito("Actualizacion exitosa");
            }
            init();
        } catch (Exception e) {
               if (e.getCause().getCause().getClass().getName().equals("org.hibernate.exception.ConstraintViolationException")) {
                if (e.getCause().getCause().getMessage().contains("could not execute statement")) {
                    Mensaje.mostrarError("El numero de factura ya existe");
                }
            }
        }
    }

    public void eliminar(Ingreso ingreso) {
        try {
            ingresoFacadeLocal.remove(ingreso);
            init();
            Mensaje.mostrarExito("Eliminacion exitosa");
        } catch (Exception e) {
            if (e.getCause().getCause().getClass().getName().equals("javax.persistence.PersistenceException")) {
                if (e.getCause().getCause().getMessage().contains("could not execute statement")) {
                    Mensaje.mostrarError("El usuario esta relacionado");
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
        Ingreso ing = (Ingreso) value;
        return ing.getNumero().toLowerCase().contains(filterText);
    }

    //metodo para agregar a la lista del detalle y validar si no hay otro producto
    public void agregarDetalle() {
        if (ingreso.esProductoDuplicado(detalleIngreso.getProducto())) {
            Mensaje.mostrarAdvertencia("El producto ya existe");
            return;
        }
        ingreso.agregarDetalle(detalleIngreso);
        detalleIngreso = new DetalleIngreso();
    }
    
    
    public void verificarNumero(){
        Ingreso ing=ingresoFacadeLocal.findByNumero(ingreso.getNumero());
        if(ing!=null){
            Mensaje.mostrarAdvertencia("Numero ya existe");
        }
    }
    

    
    //FIN DE LOS METODOS
 

}
