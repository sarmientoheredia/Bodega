/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bodcol.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Cbos- Com. Sarmiento H. Luis A.
 */
@Entity
@Table(name = "usuario")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAllCargo", query = "SELECT u FROM Usuario u WHERE (u.bodeguero='B' OR u.bodeguero='L' OR u.bodeguero='C') AND u.estado='A'"),
    @NamedQuery(name = "Usuario.findById", query = "SELECT u FROM Usuario u WHERE u.id = :id"),
    @NamedQuery(name = "Usuario.findByGrado", query = "SELECT u FROM Usuario u WHERE u.grado = :grado"),
    @NamedQuery(name = "Usuario.findByArma", query = "SELECT u FROM Usuario u WHERE u.arma = :arma"),
    @NamedQuery(name = "Usuario.findByCedula", query = "SELECT u FROM Usuario u WHERE u.cedula = :cedula"),
    @NamedQuery(name = "Usuario.findByNombre", query = "SELECT u FROM Usuario u WHERE u.nombre = :nombre"),
    @NamedQuery(name = "Usuario.findByApellido", query = "SELECT u FROM Usuario u WHERE u.apellido = :apellido"),
    @NamedQuery(name = "Usuario.findByUsuario", query = "SELECT u FROM Usuario u WHERE u.usuario = :usuario"),
    
    @NamedQuery(name = "Usuario.findByPassword", query = "SELECT u FROM Usuario u WHERE u.password = :password"),
    
    @NamedQuery(name = "Usuario.findByLogin", query = "SELECT u FROM Usuario u WHERE u.usuario = :usuario ANd u.password=:password"),
    
    
    @NamedQuery(name = "Usuario.findByRol", query = "SELECT u FROM Usuario u WHERE u.rol = :rol"),
    @NamedQuery(name = "Usuario.findByEstado", query = "SELECT u FROM Usuario u WHERE u.estado = :estado")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "grado")
    private String grado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "arma")
    private String arma;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "cedula")
    private String cedula;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "apellido")
    private String apellido;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rol")
    private Character rol;
    
    
    @Column(name = "bodeguero")
    private Character bodeguero='N';
    
    @Column(name = "estado")
    private Character estado='A';
    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST}, mappedBy = "usuario")
    private List<Ingreso> ingresoList;
    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST}, mappedBy = "usuario1")
    private List<Ingreso> ingresoList1;
    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST}, mappedBy = "usuario2")
    private List<Ingreso> ingresoList2;
    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST}, mappedBy = "usuario")
    private List<Egreso> egresoList;
    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST}, mappedBy = "usuario1")
    private List<Egreso> egresoList1;
    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST}, mappedBy = "usuario2")
    private List<Egreso> egresoList2;

    public Usuario() {
    }

    public Usuario(Integer id) {
        this.id = id;
    }

    public Usuario(Integer id, String grado, String arma, String cedula, String nombre, String apellido, String usuario, String password, Character rol) {
        this.id = id;
        this.grado = grado;
        this.arma = arma;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.usuario = usuario;
        this.password = password;
        this.rol = rol;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getArma() {
        return arma;
    }

    public void setArma(String arma) {
        this.arma = arma;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public Character getBodeguero() {
        return bodeguero;
    }

    public void setBodeguero(Character bodeguero) {
        this.bodeguero = bodeguero;
    }

    
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }
    
       public String getNombreCompleto() {
        return apellido+" "+nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Character getRol() {
        return rol;
    }

    public void setRol(Character rol) {
        this.rol = rol;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public List<Ingreso> getIngresoList() {
        return ingresoList;
    }

    public void setIngresoList(List<Ingreso> ingresoList) {
        this.ingresoList = ingresoList;
    }

    public List<Ingreso> getIngresoList1() {
        return ingresoList1;
    }

    public void setIngresoList1(List<Ingreso> ingresoList1) {
        this.ingresoList1 = ingresoList1;
    }

    public List<Ingreso> getIngresoList2() {
        return ingresoList2;
    }

    public void setIngresoList2(List<Ingreso> ingresoList2) {
        this.ingresoList2 = ingresoList2;
    }

    public List<Egreso> getEgresoList() {
        return egresoList;
    }

    public void setEgresoList(List<Egreso> egresoList) {
        this.egresoList = egresoList;
    }

    public List<Egreso> getEgresoList1() {
        return egresoList1;
    }

    public void setEgresoList1(List<Egreso> egresoList1) {
        this.egresoList1 = egresoList1;
    }

    public List<Egreso> getEgresoList2() {
        return egresoList2;
    }

    public void setEgresoList2(List<Egreso> egresoList2) {
        this.egresoList2 = egresoList2;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bodcol.entidades.Usuario[ id=" + id + " ]";
    }
    
}
