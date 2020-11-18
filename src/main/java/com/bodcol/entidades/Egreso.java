/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bodcol.entidades;

import com.bodcol.utilitarios.Mensaje;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Cbos- Com. Sarmiento H. Luis A.
 */
@Entity
@Table(name = "egreso")
@NamedQueries({
    @NamedQuery(name = "Egreso.findAll", query = "SELECT e FROM Egreso e"),
    @NamedQuery(name = "Egreso.findById", query = "SELECT e FROM Egreso e WHERE e.id = :id"),
    @NamedQuery(name = "Egreso.findByFecha", query = "SELECT e FROM Egreso e WHERE e.fecha = :fecha"),
    @NamedQuery(name = "Egreso.findByDependencia", query = "SELECT e FROM Egreso e WHERE e.dependencia = :dependencia"),
    @NamedQuery(name = "Egreso.findByTotal", query = "SELECT e FROM Egreso e WHERE e.total = :total"),
    @NamedQuery(name = "Egreso.findByEstado", query = "SELECT e FROM Egreso e WHERE e.estado = :estado")})
public class Egreso implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Size(max = 60)
    @Column(name = "dependencia")
    private String dependencia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "estado")
    private Character estado = 'A';
    @JoinColumn(name = "bodeguero_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuario;
    @JoinColumn(name = "solicitante_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuario1;
    @JoinColumn(name = "logistico_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuario2;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "egreso")
    private List<DetalleEgreso> detalleEgresoList;
    
    public Egreso() {
    }
    
    public Egreso(Integer id) {
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public String getDependencia() {
        return dependencia;
    }
    
    public void setDependencia(String dependencia) {
        this.dependencia = dependencia;
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
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Usuario getUsuario1() {
        return usuario1;
    }
    
    public void setUsuario1(Usuario usuario1) {
        this.usuario1 = usuario1;
    }
    
    public Usuario getUsuario2() {
        return usuario2;
    }
    
    public void setUsuario2(Usuario usuario2) {
        this.usuario2 = usuario2;
    }
    
    public List<DetalleEgreso> getDetalleEgresoList() {
        return detalleEgresoList;
    }
    
    public void setDetalleEgresoList(List<DetalleEgreso> detalleEgresoList) {
        this.detalleEgresoList = detalleEgresoList;
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
        if (!(object instanceof Egreso)) {
            return false;
        }
        Egreso other = (Egreso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "com.bodcol.entidades.Egreso[ id=" + id + " ]";
    }

    //metodo para controlar los productos repetidos en el detalle
    public boolean esProductoDuplicado(Producto producto) {
        if (detalleEgresoList == null) {
            return false;
        }
        return detalleEgresoList.stream().anyMatch(d -> d.getProducto().equals(producto));
    }

//    metodo que permite agregar los detalles si el detalle egreso esta en nulo 
    public void agregarDetalle(DetalleEgreso detalleEgreso) {
        if (detalleEgresoList == null) {
            detalleEgresoList = (new ArrayList<>());
        }
        //con esto agregamos las claves foreneas o hacemos la insercion en cascada
        detalleEgreso.setEgreso(this);
        detalleEgreso.setPrecio(detalleEgreso.getProducto().getPrecio());
        detalleEgreso.setTotal(detalleEgreso.getCantidad().multiply(detalleEgreso.getProducto().getPrecio()));
        detalleEgresoList.add(detalleEgreso);
        calcularTotal();
    }
    
    
    //metodo para eliminar el detalle
    public void eliminarDetalle(DetalleEgreso detalleEgreso){
        detalleEgresoList.remove(detalleEgreso);
        calcularTotal();
        Mensaje.mostrarExito("Producto eliminado del detalle");
    }
    
    //metod que me permite calcular el total
    public void calcularTotal(){
        total=BigDecimal.ZERO;
        for(DetalleEgreso dt:detalleEgresoList){
            total=total.add(dt.getSubTotal());
        }
    }
}
