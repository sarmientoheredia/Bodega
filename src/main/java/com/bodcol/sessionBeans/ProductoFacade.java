/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bodcol.sessionBeans;

import com.bodcol.entidades.Producto;
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
public class ProductoFacade extends AbstractFacade<Producto> implements ProductoFacadeLocal {

    @PersistenceContext(unitName = "bodegaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductoFacade() {
        super(Producto.class);
    }

    @Override
    public Producto findByNombre(String nombre) {
        Query q=em.createNamedQuery("Producto.findByNombre", Producto.class);
        q.setParameter("nombre", nombre);
        try {
            return (Producto) q.getSingleResult();
        } catch (NoResultException  e) {
            Mensaje.mostrarExito("Producto aun no registrado");
            return null;
        }
        
    }

    @Override
    public List<Producto> findAllActivo() {
        Query q=em.createNamedQuery("Producto.findAllActivo", Producto.class);
        return q.getResultList();
    }
    
}
