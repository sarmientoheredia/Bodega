/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bodcol.sessionBeans;

import com.bodcol.entidades.Proveedor;
import com.bodcol.utilitarios.Mensaje;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Cbos- Com. Sarmiento H. Luis A.
 */
@Stateless
public class ProveedorFacade extends AbstractFacade<Proveedor> implements ProveedorFacadeLocal {

    @PersistenceContext(unitName = "bodegaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProveedorFacade() {
        super(Proveedor.class);
    }

    @Override
    public Proveedor findByCedula(String cedula) {
        Query q = em.createNamedQuery("Proveedor.findByCedula", Proveedor.class);
        q.setParameter("cedula", cedula);
        try {
            return (Proveedor) q.getSingleResult();
        } catch (NoResultException e) {
            Mensaje.mostrarExito("Cédula válida");
            return null;
        }
    }

    @Override
    public Proveedor findByRuc(String ruc) {
        Query q = em.createNamedQuery("Proveedor.findByRuc", Proveedor.class);
        q.setParameter("ruc", ruc);
        try {
            return (Proveedor) q.getSingleResult();
        } catch (NoResultException e) {
            Mensaje.mostrarExito("Ruc válido");
            return null;
        }
    }

    @Override
    public Proveedor findByRucNatural(String rucNatural) {
        Query q = em.createNamedQuery("Proveedor.findByRucNatural", Proveedor.class);
        q.setParameter("rucNatural", rucNatural);
        try {
            return (Proveedor) q.getSingleResult();
        } catch (NoResultException e) {
            Mensaje.mostrarExito("Ruc válido");
            return null;
        }
    }

    @Override
    public List<Proveedor> findAllActivo() {
        Query q=em.createNamedQuery("Proveedor.findAllActivo", Proveedor.class);
        return q.getResultList();
    }

}
