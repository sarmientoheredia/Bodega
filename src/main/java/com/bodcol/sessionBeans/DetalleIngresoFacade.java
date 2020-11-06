package com.bodcol.sessionBeans;

import com.bodcol.entidades.DetalleIngreso;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Cbos- Com. Sarmiento H. Luis A.
 */
@Stateless
public class DetalleIngresoFacade extends AbstractFacade<DetalleIngreso> implements DetalleIngresoFacadeLocal {

    @PersistenceContext(unitName = "bodegaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetalleIngresoFacade() {
        super(DetalleIngreso.class);
    }
    
}
