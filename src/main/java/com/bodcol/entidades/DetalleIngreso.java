package com.bodcol.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "detalle_ingreso")
@NamedQueries({
    @NamedQuery(name = "DetalleIngreso.findAll", query = "SELECT d FROM DetalleIngreso d"),
    @NamedQuery(name = "DetalleIngreso.findById", query = "SELECT d FROM DetalleIngreso d WHERE d.id = :id"),
    @NamedQuery(name = "DetalleIngreso.findByCantidad", query = "SELECT d FROM DetalleIngreso d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "DetalleIngreso.findByPrecio", query = "SELECT d FROM DetalleIngreso d WHERE d.precio = :precio"),
    @NamedQuery(name = "DetalleIngreso.findByTotal", query = "SELECT d FROM DetalleIngreso d WHERE d.total = :total"),
    @NamedQuery(name = "DetalleIngreso.findByEstado", query = "SELECT d FROM DetalleIngreso d WHERE d.estado = :estado")})
public class DetalleIngreso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad")
    private BigDecimal cantidad;
    @Column(name = "precio")
    private BigDecimal precio;
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "estado")
    private Character estado = 'A';
    @JoinColumn(name = "producto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Producto producto;
    @JoinColumn(name = "ingreso_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Ingreso ingreso;

    public DetalleIngreso() {
    }

    public DetalleIngreso(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Ingreso getIngreso() {
        return ingreso;
    }

    public void setIngreso(Ingreso ingreso) {
        this.ingreso = ingreso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleIngreso)) {
            return false;
        }
        DetalleIngreso other = (DetalleIngreso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bodcol.entidades.DetalleIngreso[ id=" + id + " ]";
    }

//    //METODO PARA CALCULAR EL SUBTOTAL DE EL DETALLE DE LA FACTURA
    public BigDecimal getSubTotal() {
        if (cantidad == null || precio==null) {
            return BigDecimal.ZERO;
        }
        return cantidad.multiply(precio);
//        return precio.multiply(BigDecimal.valueOf(cantidad));
    }
}
