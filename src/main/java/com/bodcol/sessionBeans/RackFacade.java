/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bodcol.sessionBeans;

import com.bodcol.entidades.Rack;
import com.bodcol.entidades.Seccion;
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
        Query q = em.createNamedQuery("Rack.findByNombre", Rack.class);
        q.setParameter("nombre", nombre);
        try {
            return (Rack) q.getSingleResult();
        } catch (NoResultException e) {
            Mensaje.mostrarExito("Estateria valida");
            return null;
        }
    }

    @Override
    public List<Rack> findAllSeccion(Seccion seccion) {
        Query q = em.createNamedQuery("Rack.findAllSeccion", Rack.class);
        q.setParameter("seccion", seccion);
        return q.getResultList();
    }

}
