/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bodcol.sessionBeans;

import com.bodcol.entidades.Egreso;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Cbos- Com. Sarmiento H. Luis A.
 */
@Stateless
public class EgresoFacade extends AbstractFacade<Egreso> implements EgresoFacadeLocal {

    @PersistenceContext(unitName = "bodegaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EgresoFacade() {
        super(Egreso.class);
    }

    @Override
    public List<Egreso> findDate(Object date, Object date1) {
        Query q=em.createNamedQuery("Egreso.findDate", Egreso.class);
        q.setParameter("fecha", date);
        q.setParameter("fecha1", date1);
        return q.getResultList();
    }
    
}
