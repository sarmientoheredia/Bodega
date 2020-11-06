package com.bodcol.sessionBeans;

import com.bodcol.entidades.Seccion;
import com.bodcol.utilitarios.Mensaje;
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
public class SeccionFacade extends AbstractFacade<Seccion> implements SeccionFacadeLocal {

    @PersistenceContext(unitName = "bodegaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SeccionFacade() {
        super(Seccion.class);
    }

    @Override
    public Seccion findByNombre(String nombre) {
        Query q = em.createNamedQuery("Seccion.findByNombre", Seccion.class);
        q.setParameter("nombre", nombre);
        try {
            return (Seccion) q.getSingleResult();
        } catch (NoResultException e) {
            Mensaje.mostrarExito("Seccion valida");
            return null;
        }
    }

}
