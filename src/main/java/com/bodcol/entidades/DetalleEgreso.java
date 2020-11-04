/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author Cbos- Com. Sarmiento H. Luis A.
 */
@Entity
@Table(name = "detalle_egreso")
@NamedQueries({
    @NamedQuery(name = "DetalleEgreso.findAll", query = "SELECT d FROM DetalleEgreso d"),
    @NamedQuery(name = "DetalleEgreso.findById", query = "SELECT d FROM DetalleEgreso d WHERE d.id = :id"),
    @NamedQuery(name = "DetalleEgreso.findByCantidad", query = "SELECT d FROM DetalleEgreso d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "DetalleEgreso.findByPrecio", query = "SELECT d FROM DetalleEgreso d WHERE d.precio = :precio"),
    @NamedQuery(name = "DetalleEgreso.findByTotal", query = "SELECT d FROM DetalleEgreso d WHERE d.total = :total"),
    @NamedQuery(name = "DetalleEgreso.findByEstado", query = "SELECT d FROM DetalleEgreso d WHERE d.estado = :estado")})
public class DetalleEgreso implements Serializable {

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
    private Character estado='A';
    @JoinColumn(name = "producto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Producto producto;
    @JoinColumn(name = "egreso_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Egreso egreso;

    public DetalleEgreso() {
    }

    public DetalleEgreso(Integer id) {
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

    public Egreso getEgreso() {
        return egreso;
    }

    public void setEgreso(Egreso egreso) {
        this.egreso = egreso;
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
        if (!(object instanceof DetalleEgreso)) {
            return false;
        }
        DetalleEgreso other = (DetalleEgreso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bodcol.entidades.DetalleEgreso[ id=" + id + " ]";
    }
    
}
