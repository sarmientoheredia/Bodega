package com.bodcol.sessionBeans;

import com.bodcol.entidades.Ingreso;
import com.bodcol.utilitarios.Mensaje;
import java.util.Date;
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
public class IngresoFacade extends AbstractFacade<Ingreso> implements IngresoFacadeLocal {

    @PersistenceContext(unitName = "bodegaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IngresoFacade() {
        super(Ingreso.class);
    }

    @Override
    public Ingreso findByNumero(String numero) {
        Query q = em.createNamedQuery("Ingreso.findByNumero", Ingreso.class);
        q.setParameter("numero", numero);
        try {
            return (Ingreso) q.getSingleResult();
        } catch (NoResultException e) {
            Mensaje.mostrarExito("Número valido");
            return null;
        }
    }

    @Override
    public List<Ingreso> findDate(Object date, Object date1) {
        Query q=em.createNamedQuery("Ingreso.findDate", Ingreso.class);
        q.setParameter("fecha", date);
        q.setParameter("fecha1", date1);
        
        return q.getResultList();
    }

}
