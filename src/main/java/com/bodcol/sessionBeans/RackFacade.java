/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bodcol.sessionBeans;

import com.bodcol.entidades.Rack;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Cbos- Com. Sarmiento H. Luis A.
 */
@Stateless
public class RackFacade extends AbstractFacade<Rack> implements RackFacadeLocal {

    @PersistenceContext(unitName = "bodegaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RackFacade() {
        super(Rack.class);
    }

    @Override
    public Rack findByNombre(String nombre) {
        Query q=em.createNamedQuery("Rack.findByNombre", Rack.class);
        q.setParameter("nombre", nombre);
        return (Rack) q.getSingleResult();
    }
    
}
