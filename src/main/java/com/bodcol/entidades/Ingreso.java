package com.bodcol.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
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
@Table(name = "ingreso")
@NamedQueries({
    @NamedQuery(name = "Ingreso.findAll", query = "SELECT i FROM Ingreso i"),
    @NamedQuery(name = "Ingreso.findById", query = "SELECT i FROM Ingreso i WHERE i.id = :id"),
    @NamedQuery(name = "Ingreso.findByFecha", query = "SELECT i FROM Ingreso i WHERE i.fecha = :fecha"),
    @NamedQuery(name = "Ingreso.findByNumero", query = "SELECT i FROM Ingreso i WHERE i.numero = :numero"),
    @NamedQuery(name = "Ingreso.findByTotal", query = "SELECT i FROM Ingreso i WHERE i.total = :total"),
    @NamedQuery(name = "Ingreso.findByEstado", query = "SELECT i FROM Ingreso i WHERE i.estado = :estado")})
public class Ingreso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Size(max = 80)
    @Column(name = "numero")
    private String numero;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "estado")
    private Character estado = 'A';
    @JoinColumn(name = "cpublicas_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuario;
    @JoinColumn(name = "logistico_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuario1;
    @JoinColumn(name = "bodeguero_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuario2;
    @JoinColumn(name = "proveedor_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Proveedor proveedor;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "ingreso")
    private List<DetalleIngreso> detalleIngresoList;

    public Ingreso() {
    }

    public Ingreso(Integer id) {
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public List<DetalleIngreso> getDetalleIngresoList() {
        return detalleIngresoList;
    }

    public void setDetalleIngresoList(List<DetalleIngreso> detalleIngresoList) {
        this.detalleIngresoList = detalleIngresoList;
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
        if (!(object instanceof Ingreso)) {
            return false;
        }
        Ingreso other = (Ingreso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bodcol.entidades.Ingreso[ id=" + id + " ]";
    }

    //metodo para controlar los prodcutos repetidos
    public boolean esProductoDuplicado(Producto producto) {

        //enymatch verifica si alguien coincide con el valor a ingresosa
        if (detalleIngresoList == null) {
            return false;
        }
        return detalleIngresoList.stream().anyMatch(d -> d.getProducto().equals(producto));
    }

    //metodo que permite agregar los detalles si el detalle factura esta en nulo 
    public void agregarDetalle(DetalleIngreso detalleIngreso) {
        if (detalleIngresoList == null) {
            detalleIngresoList = (new ArrayList<>());
        }
        //con esto agregamos las claves foraneas o hacemos la insercion en cascada
        detalleIngreso.setIngreso(this);
        detalleIngreso.setTotal(detalleIngreso.getCantidad().multiply(detalleIngreso.getPrecio()));
        detalleIngresoList.add(detalleIngreso);
        calcularTotal();
    }

    //metdo para eliminar el detalle 
    public void eliminarDetalle(DetalleIngreso detalleIngreso) {
        detalleIngresoList.remove(detalleIngreso);
        calcularTotal();
    }

    //metodo para que me sume todos los subtotales de los detalles del ingreso y  mñe muestre en la factura del ingreso
    public BigDecimal getTotal() {
        return total;
        // return total= BigDecimal.valueOf(detalleIngresoList.stream().mapToDouble(d -> d.getTotal().doubleValue()).sum());
//        return BigDecimal.valueOf(detalleIngresoList.stream().mapToDouble(d -> d.getTotal().doubleValue()).sum());
    }

    public void calcularTotal() {
        total = BigDecimal.ZERO;
        for (DetalleIngreso item : detalleIngresoList) {
            total = total.add(item.getSubTotal());
        }
    }
}
